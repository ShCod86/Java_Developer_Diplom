package ru.netology.cloudstorage.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.cloudstorage.DTO.AuthenticationRequest;
import ru.netology.cloudstorage.DTO.AuthenticationResponse;
import ru.netology.cloudstorage.entity.File;
import ru.netology.cloudstorage.exception.DuplicateFileNameException;
import ru.netology.cloudstorage.exception.SessionException;
import ru.netology.cloudstorage.DTO.FileData;
import ru.netology.cloudstorage.service.AuthenticationService;
import ru.netology.cloudstorage.service.FileService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CloudStorageController {
    private final AuthenticationService authenticationService;
    private final FileService fileService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.login(authenticationRequest);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("auth-token") String authToken) {
        boolean mark = authenticationService.logout(authToken);
        if (!mark) {
            throw new SessionException("User not found");
        }
        return ResponseEntity.ok().body(null);
    }

    @SneakyThrows
    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestHeader("auth-token") @NotNull String authToken,
                                             @RequestParam("filename") @NotNull String fileName,
                                             @RequestBody @NotNull MultipartFile file) {
        boolean mark = fileService.uploadFile(authToken, fileName, file.getContentType(), file.getBytes(), file.getSize());
        if (!mark) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("File uploaded");
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestHeader("auth-token") @NotNull String authToken,
                                             @RequestParam("filename") @NotNull String fileName) {
        String response = fileService.deleteFile(authToken, fileName);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestHeader("auth-token") @NotNull String authToken,
                                          @RequestParam("filename") @NotNull String fileName) {
        File uploadFile = fileService.getFile(authToken, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + uploadFile.getFileName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(uploadFile.getContent());
    }

    @GetMapping("/file")
    public ResponseEntity<String> renameFile(@RequestHeader("auth-token") @NotNull String authToken,
                                             @RequestParam("filename") @NotNull String fileName,
                                             @RequestParam("newFileName") @NotNull String newFileName) {
        boolean mark = fileService.renameFile(authToken, fileName, newFileName);
        if (!mark) {
            throw new DuplicateFileNameException("A file with the same name already exists");
        }
        return ResponseEntity.ok().body(fileName.concat(" was change to ".concat(newFileName)));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileData>> getAllFiles(@RequestHeader("auth-token") @NotNull String authToken,
                                                      @RequestParam("limit") @NotNull int limit) {
        List<FileData> listFiles = fileService.getFiles(authToken, limit);
        return ResponseEntity.ok().body(listFiles);
    }
}
