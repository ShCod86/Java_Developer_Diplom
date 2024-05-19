package ru.netology.cloudstorage.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileData {
    @NotNull
    private String fileName;
    @NotNull
    private Long size;
}
