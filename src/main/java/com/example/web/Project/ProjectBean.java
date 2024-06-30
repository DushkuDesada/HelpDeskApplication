package com.example.web.Project;

import com.example.domain.Project;
import com.example.domain.User;
import com.example.domain.UserRole;
import com.example.ejb.EjbProjectRepository;
import com.example.ejb.EjbUserRepository;
import com.example.web.User.LoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SessionScoped
@Named("projectBean")
public class ProjectBean implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    EjbProjectRepository projectRepository;

    @Inject
    EjbUserRepository userRepository;

    @Inject
    private LoginBean loginBean; // Inject LoginBean

    private List<Project> projects;
    private ProjectForm form;
    private List<User> customerUsers;
    private int currentPage = 1;
    private int itemsPerPage = 10;

    @PostConstruct
    public void init() {
        this.form = new ProjectForm();
        loadProjects();
        loadCustomerUsers();
    }

    public void loadProjects() {

        if (loginBean.getCurrentUser().getRole() == UserRole.CUSTOMER) {
            this.projects = projectRepository.findByUser(loginBean.getCurrentUser().getId());
        }else {
            this.projects = projectRepository.findAll();
        }
    }

    public void loadCustomerUsers() {
        this.customerUsers = userRepository.findByCustomerRole();
    }

    public List<User> getCustomerUsers() {
        return customerUsers;
    }

    public void saveProject() {

        User owner = userRepository.findById(UUID.fromString(form.getOwnerId()));
        System.out.println("name:"+ form.getName());
        System.out.println("owner:"+ owner);
        if (owner == null) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid owner ID.", null));
            return;
        }

        if (this.form.getId() == null) {
            projectRepository.save(new Project(form.getName(), owner));
        } else {
            Project existing = projectRepository.findById(this.form.getId());
            existing.setName(this.form.getName());
            existing.setOwner(owner);
            projectRepository.save(existing);
        }
        loadProjects();
        this.form = new ProjectForm();
        facesContext.addMessage(null, new FacesMessage("Project was saved successfully."));
    }

    public void editProject(UUID id) {
        Project project = projectRepository.findById(id);
        this.form = new ProjectForm(project.getId(), project.getName(), project.getOwner().getId().toString());
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
        loadProjects();
        facesContext.addMessage(null, new FacesMessage("Project was deleted successfully."));
    }
    public List<Project> getPaginatedProjects() {
        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, projects.size());
        return projects.subList(fromIndex, toIndex);
    }

    public List<Integer> getPageNumbers() {
        List<Integer> pageNumbers = new ArrayList<>();
        int totalPages = (int) Math.ceil((double) projects.size() / itemsPerPage);
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        return pageNumbers;
    }

    public void changePage(int pageNumber) {
        currentPage = pageNumber;
    }

    // Getters and Setters for form and projects
    public ProjectForm getForm() {
        return form;
    }

    public void setForm(ProjectForm form) {
        this.form = form;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
