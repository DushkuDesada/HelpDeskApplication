package com.example.web.Working;

import java.util.Date;
import java.util.UUID;

public class WorkingForm {
    private UUID Id;
    private String user;
    private String project;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    private String task;
    private String dayOfWeek;

    public WorkingForm() {
    }

    public WorkingForm(UUID id, String user, String project, String task, String dayOfWeek, Date startTime, Date endTime) {
        Id = id;
        this.user = user;
        this.project = project;
        this.task = task;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public WorkingForm(String user, String project, String task, String dayOfWeek, Date startTime, Date endTime) {
        this.user = user;
        this.project = project;
        this.task = task;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private Date startTime;
    private Date endTime;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}