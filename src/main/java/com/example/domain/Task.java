package com.example.domain;

import jakarta.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task extends AbstractEntity<UUID> {
    private String title;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User assignedTo;

    @OneToMany(mappedBy = "task")
    private List<TaskComment> comments;

    @OneToMany(mappedBy = "task")
    private List<WorkingHour> workingHours;

    // Default constructor
    public Task() {}

    // Parameterized constructor
    public Task(String title, TaskType type, TaskStatus status, Project project, User createdBy, User assignedTo) {
        this.title = title;
        this.type = type;
        this.status = status;
        this.project = project;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<TaskComment> getComments() {
        return comments;
    }

    public void setComments(List<TaskComment> comments) {
        this.comments = comments;
    }

    public List<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    // Method to get the latest comment
    public TaskComment getLatestComment() {
        return comments.stream()
                .max(Comparator.comparing(TaskComment::getCreatedAt))
                .orElse(null);
    }
}
