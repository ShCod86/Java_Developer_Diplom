package ru.netology.cloudstorage.exception;

public class DuplicateFileNameException extends RuntimeException {
    public DuplicateFileNameException(String msg) {
        super(msg);
    }
}
