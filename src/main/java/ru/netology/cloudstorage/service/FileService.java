package ru.netology.cloudstorage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.File;
import ru.netology.cloudstorage.entity.User;
import ru.netology.cloudstorage.exception.DuplicateFileNameException;
import ru.netology.cloudstorage.exception.FileNotFoundException;
import ru.netology.cloudstorage.exception.InputDataException;
import ru.netology.cloudstorage.exception.SessionException;
import ru.netology.cloudstorage.DTO.FileData;
import ru.netology.cloudstorage.model.Session;
import ru.netology.cloudstorage.repositiry.FileRepository;
import ru.netology.cloudstorage.repositiry.UsersRepository;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class FileService {
    private FileRepository fileRepository;
    private UsersRepository usersRepository;
    private AuthenticationService authenticationService;

    public Long chekUser(String authToken) {
        Session result = authenticationService.getSession(authToken);
        if (result == null) {
            log.error("User not found");
            throw new SessionException("User not found");
        }
        return Objects.requireNonNull(result).getUserId();
    }

    public File chekFile(Long userId, String fileName) {
        var checkFile = fileRepository.findFileByUserIdAndFileName(userId, fileName);
        if (checkFile.isEmpty()) {
            log.error("File not found");
            throw new FileNotFoundException("File not found");
        }
        return checkFile.get();

    }
    public boolean uploadFile(String authToken, String fileName, String contentType, byte[] content, long fileSize) {
        Long userId = chekUser(authToken);
        File file;
        if (fileRepository.findFileByUserIdAndFileName(userId,fileName).isPresent()) {
            log.error("File already exists.");
            throw new InputDataException("File already exists.");
        }
        User user = usersRepository.getReferenceById(userId);
        file = File.builder()
                .fileName(fileName)
                .type(contentType)
                .content(content)
                .user(user)
                .build();
        fileRepository.save(file);
        log.info("User ".concat(userId.toString()).concat("successfully uploaded the file ").concat(fileName));
        return true;
    }

    public String deleteFile(String authToken, String fileName) {
        Long userId = chekUser(authToken);
        File file = chekFile(userId, fileName);
        fileRepository.deleteById(file.getId());
        log.info("User ".concat(userId.toString()).concat("successfully delete the file ").concat(fileName));
        return "File deleted.";
    }
    public File getFile(String authToken, String fileName) {
        Long userId = chekUser(authToken);
        File file = chekFile(userId, fileName);
        log.info("User ".concat(String.valueOf(userId)).concat("successfully download the file ").concat(fileName));
        return file;
    }
    public boolean renameFile(String authToken, String fileName, String newFileName) {
        Long userId = chekUser(authToken);
        File file = chekFile(userId, fileName);
        if (fileRepository.findFileByUserIdAndFileName(userId, newFileName).isPresent()) {
            log.error("A file with the same name already exists");
            throw new DuplicateFileNameException("A file with the same name already exists");
        }
        file.setFileName(newFileName);
        fileRepository.save(file);
        log.info("file ".concat(fileName).concat( "successfully renamed. New File name").concat(newFileName));
        return true;
    }
    public List<File> getFiles(String authToken, int limit) {
        Long userId = chekUser(authToken);
        if (limit < 0) {
            log.warn("Wrong limit.");
            throw new InputDataException("Wrong limit.");
        }
        List<File> allFiles = fileRepository.findFilesByUserId(userId);
        log.info("received a list of user files ".concat(String.valueOf(userId)));
        return allFiles;
    }
}
