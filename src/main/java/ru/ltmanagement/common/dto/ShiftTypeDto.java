package ru.ltmanagement.common.dto;

import java.util.Arrays;

public enum ShiftTypeDto {

    FIRST ("1"),
    SECOND("2");

    private final String code;

    ShiftTypeDto(String code){
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public static ShiftTypeDto of(String code) {
        return Arrays.stream(ShiftTypeDto.values())
                .filter(status -> status.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find Shift type with code " + code));
    }

}