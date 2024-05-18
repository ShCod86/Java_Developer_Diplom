package ru.netology.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Session {
    private String id;
    private long userId;
}
