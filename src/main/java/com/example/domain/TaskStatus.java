package com.example.domain;

public enum TaskStatus {
    NEW,
    PROGRESS,
    APPROVED,
    REJECTED,
    COMPLETED;

    public static TaskStatus fromString(String text) {
        for (TaskStatus type : TaskStatus.values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null; // Or throw an IllegalArgumentException
    }
}
