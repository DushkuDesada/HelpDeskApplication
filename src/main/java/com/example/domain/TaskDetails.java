package com.example.domain;


import com.example.domain.Task;
import com.example.domain.User;

public class TaskDetails {

    private Task task;
    private User projectCreator;
    private Long totalHoursWorked;

    public TaskDetails(Task task, User projectCreator, Long totalHoursWorked) {
        this.task = task;
        this.projectCreator = projectCreator;
        this.totalHoursWorked = totalHoursWorked;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(User projectCreator) {
        this.projectCreator = projectCreator;
    }

    public Long getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(Long totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }
}