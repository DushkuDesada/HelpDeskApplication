package com.example.web.Task;

import com.example.domain.*;
import com.example.ejb.EjbProjectRepository;
import com.example.ejb.EjbTaskCommentRepository;
import com.example.ejb.EjbTaskRepository;
import com.example.ejb.EjbUserRepository;
import com.example.web.User.LoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@ViewScoped
@Named("taskBean")
public class TaskBean implements Serializable {

    @Inject
    private FacesContext facesContext;

    @Inject
    private EjbTaskRepository taskRepository;

    @Inject
    private EjbProjectRepository projectRepository;

    @Inject
    private EjbUserRepository userRepository;

    @Inject
    private EjbTaskCommentRepository taskCommentRepository;

    @Inject
    private LoginBean loginBean; // Inject LoginBean

    private List<Task> tasks;
    private TaskForm form;
    private int currentPage = 1;
    private int itemsPerPage = 10;
    private String comment;
    private boolean isEditing;

    @PostConstruct
    public void init() {
        this.form = new TaskForm();
        loadTasks();
    }

    public void loadTasks() {
        if(loginBean.getCurrentUser().getRole() == UserRole.ADMIN ) {
            this.tasks = taskRepository.findAll();
        } else if (loginBean.getCurrentUser().getRole() == UserRole.CUSTOMER ) {
            this.tasks = taskRepository.findByCustomer(loginBean.getCurrentUser().getId());
        } else{
            this.tasks = taskRepository.findByUser(loginBean.getCurrentUser().getId());
        }
    }

    public List<Project> getProjects() {

        return projectRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<TaskStatus> getTaskStatus() {
        return Arrays.asList(TaskStatus.values());
    }

    public List<TaskType> getTaskType() {
        return Arrays.asList(TaskType.values());
    }

    public List<TaskStatus> getFilteredTaskStatus() {
        List<TaskStatus> statuses = new ArrayList<>(Arrays.asList(TaskStatus.NEW, TaskStatus.PROGRESS, TaskStatus.COMPLETED));

        if (isEditing() && form.getStatus().equals(TaskStatus.COMPLETED.toString()) && loginBean.getCurrentUser().getRole() == UserRole.CUSTOMER) {
            statuses.clear(); // Remove all statuses
            statuses.add(TaskStatus.APPROVED);
            statuses.add(TaskStatus.REJECTED);
        }else if(isEditing() && loginBean.getCurrentUser().getRole() == UserRole.CUSTOMER) {
            statuses.clear(); // Remove all statuses if not completed
        }

        return statuses;
    }

    public void saveTask() {
        Project project = projectRepository.findById(UUID.fromString(form.getProject()));
        if (project == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Project.", null));
            return;
        }
        User assignedTo = userRepository.findById(UUID.fromString(form.getAssignedTo()));
        if (assignedTo == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid owner ID.", null));
            return;
        }
        User createdBy = loginBean.getCurrentUser();
        if (createdBy == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please login.", null));
            return;
        }
        if (TaskStatus.fromString(form.getStatus()) == TaskStatus.REJECTED && loginBean.getCurrentUser().getRole() == UserRole.CUSTOMER) {
            form.setStatus(new String("PROGRESS"));

        }
        Task task;
        if (this.form.getId() == null) {
            task = new Task(form.getTitle(), TaskType.fromString(form.getType()), TaskStatus.fromString(form.getStatus()), project, createdBy, assignedTo);
            taskRepository.save(task);
        } else {
            task = taskRepository.findById(this.form.getId());
            task.setTitle(this.form.getTitle());
            task.setType(TaskType.fromString(form.getType()));
            task.setStatus(TaskStatus.fromString(form.getStatus()));
            task.setProject(project);
            task.setCreatedBy(createdBy);
            task.setAssignedTo(assignedTo);
            taskRepository.save(task);
        }
        System.out.print("print"+form.getComment());

        // Save the comment if it exists
        if (form.getComment() != null && !form.getComment().isEmpty()) {
            TaskComment taskComment = new TaskComment();
            taskComment.setTask(task);
            taskComment.setUser(createdBy);
            taskComment.setComment(form.getComment());
            taskCommentRepository.save(taskComment);
            System.out.print("saved");
        }

        loadTasks();
        resetForm();
        facesContext.addMessage(null, new FacesMessage("Task was saved successfully."));
    }

    public void editTask(UUID id) {
        Task task = taskRepository.findById(id);
        // Fetch the latest comment for the task
        TaskComment latestComment = taskCommentRepository.findLatestCommentByTaskId(id);
            this.form = new TaskForm(task.getId(), task.getTitle(), task.getType().toString(), task.getStatus().toString(), task.getProject().getId().toString(), task.getCreatedBy().getId().toString(), task.getAssignedTo().getId().toString(), latestComment.getComment());
            setEditing(true);

    }



    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
        loadTasks();
        resetForm();
        facesContext.addMessage(null, new FacesMessage("Task was deleted successfully."));
    }




    public List<Task> getPaginatedTask() {
        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, tasks.size());
        return tasks.subList(fromIndex, toIndex);
    }

    public List<Integer> getPageNumbers() {
        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = (int) Math.ceil((double) tasks.size() / itemsPerPage);
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }

    public void changePage(int pageNumber) {
        currentPage = pageNumber;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public TaskForm getForm() {
        return form;
    }

    public void setForm(TaskForm form) {
        this.form = form;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private void resetForm() {
        this.form = new TaskForm();
        this.comment = null;
        setEditing(false);
    }
}