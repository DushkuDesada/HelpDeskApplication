package com.example.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "task_comments")
public class TaskComment extends AbstractEntity<UUID> {
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant createdAt;

    // Default constructor
    public TaskComment() {
    }

    // Parameterized constructor
    public TaskComment(Task task, User user, String comment, Instant createdAt) {
        this.task = task;
        this.user = user;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
