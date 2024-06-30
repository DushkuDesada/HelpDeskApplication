package com.example.domain;

public enum TaskType {
    BUG,
    ENHANCEMENT,
    UPGRADE;

    public static TaskType fromString(String text) {
        for (TaskType type : TaskType.values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null; // Or throw an IllegalArgumentException
    }
}
