package ru.netology.cloudstorage.utility;

public class IdGen {
    public static String generateId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
