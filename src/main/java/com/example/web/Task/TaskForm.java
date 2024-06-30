package com.example.web.Task;

import com.example.domain.*;

import java.util.UUID;

public class TaskForm {

    private UUID id;
    private String title;
    private String type;
    private String status;
    private String project;
    private String createdBy;
    private String assignedTo;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public TaskForm() {}

    public TaskForm(UUID id, String title, String type, String status, String project, String createdBy, String assignedTo, String comment) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.project = project;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.comment = comment;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
