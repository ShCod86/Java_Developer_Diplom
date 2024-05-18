package ru.netology.cloudstorage.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


@AllArgsConstructor
@Data
@Builder
public class AuthenticationRequest {
    @NonNull
    private String login;
    @NonNull
    private String password;
}
