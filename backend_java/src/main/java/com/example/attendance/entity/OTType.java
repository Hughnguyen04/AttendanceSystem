package com.example.attendance.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OTType {
    NORMAL_DAY("normal_day", 1.5),
    WEEKEND_DAY("weekend_day", 2.0),
    HOLIDAY_DAY("holiday_day", 3.0);

    private final String value;
    private final double multiplier;

    OTType(String value, double multiplier) {
        this.value = value;
        this.multiplier = multiplier;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @JsonCreator
    public static OTType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (OTType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown OTType: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}

