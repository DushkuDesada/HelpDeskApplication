package com.example.domain;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
    public static DayOfWeek fromString(String text) {
        for (DayOfWeek type : DayOfWeek.values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null; // Or throw an IllegalArgumentException
    }
}
