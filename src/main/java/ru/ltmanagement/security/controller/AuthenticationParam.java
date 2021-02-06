package ru.ltmanagement.security.controller;

import java.util.Arrays;
import java.util.Optional;

public enum AuthenticationParam {
    USERNAME("username"),
    PASSWORD("password"),
    TOKEN("Authorization");

    private final String code;

    AuthenticationParam(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AuthenticationParam of(String code) {
        return find(code)
            .orElseThrow(() -> new IllegalArgumentException("Failed to find authentication header with code " + code));
    }

    public static Optional<AuthenticationParam> find(String code) {
        return Arrays.stream(AuthenticationParam.values())
            .filter(value -> value.code.equalsIgnoreCase(code))
            .findAny();
    }
}
