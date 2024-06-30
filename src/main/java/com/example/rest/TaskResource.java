package com.example.rest;

import com.example.domain.*;
import com.example.ejb.EjbProjectRepository;
import com.example.ejb.EjbTaskRepository;
import com.example.ejb.EjbUserRepository;
import com.example.ejb.EjbWorkingHourRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    @Inject
    private EjbUserRepository userRepository;

    @Inject
    private EjbTaskRepository taskRepository;

    @Inject
    private EjbProjectRepository projectRepository;

    @Inject
    private EjbWorkingHourRepository workingHourRepository;

    @POST
    @Path("/user-tasks")
    public Response getUserTasks(UserCredentials credentials) {
        // Check if the credentials are valid
        User user = userRepository.findByUsername(credentials.getUsername());
        if (user == null || !user.getPassword().equals(credentials.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password")
                    .build();
        }

        // Fetch projects for the user
        List<Project> projects = projectRepository.findByUser(user.getId());

        // List to hold the tasks with status and total worked minutes
        List<TaskResponse> taskResponses = new ArrayList<>();

        // Fetch tasks for each project
        for (Project project : projects) {
            List<Task> tasks = taskRepository.findByProject(project.getId());

            for (Task task : tasks) {
                List<WorkingHour> workingHours = workingHourRepository.findByTask(task.getId());
                long totalHours = 0;

                for (WorkingHour workingHour : workingHours) {
                    totalHours += workingHour.getDurationInHours();
                }

                // Assuming task has a getStatus() method to fetch its status
                TaskStatus status = task.getStatus();

                // Add task response to the list
                taskResponses.add(new TaskResponse(task.getId(), task.getTitle(), status, totalHours));
            }
        }

        return Response.ok(taskResponses).build();
    }

    // Define a simple class to represent user credentials
    public static class UserCredentials {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public class TaskResponse {
        private UUID taskId;
        private String taskName;
        private TaskStatus status;
        private long totalWorkedHours;

        public TaskResponse(UUID taskId, String taskName, TaskStatus status, long totalWorkedHours) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.status = status;
            this.totalWorkedHours = totalWorkedHours;
        }

        // Getters and Setters
        public UUID getTaskId() {
            return taskId;
        }

        public void setTaskId(UUID taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public TaskStatus getStatus() {
            return status;
        }

        public void setStatus(TaskStatus status) {
            this.status = status;
        }

        public long getTotalWorkedHours() {
            return totalWorkedHours;
        }

        public void setTotalWorkedHours(long totalWorkedHours) {
            this.totalWorkedHours = totalWorkedHours;
        }
    }

}