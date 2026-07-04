package com.example.attendance.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AbsenceType {
    ANNUAL("annual"),
    MATERNITY("maternity"),
    WEDDING("wedding"),
    FUNERAL("funeral"),
    PATERNITY("paternity");

    private final String value;

    AbsenceType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AbsenceType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (AbsenceType type : values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Loại nghỉ không hợp lệ: " + value);
    }
}
