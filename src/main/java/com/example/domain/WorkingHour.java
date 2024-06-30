package com.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Entity
@Table(name = "working_hours")
public class WorkingHour extends AbstractEntity<UUID> {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private Date startTime;

    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    public long getDurationInMinutes() {
        return (endTime.getTime() - startTime.getTime()) / (1000 * 60);
    }
    public long getDurationInHours() {
        long durationInMillis = endTime.getTime() - startTime.getTime();
        return TimeUnit.MILLISECONDS.toHours(durationInMillis);
    }


    @ManyToOne
    private User user;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne
    private Task task;

    @ManyToOne
    private Project project;
    // Getters and Setters
}