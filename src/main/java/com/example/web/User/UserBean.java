package com.example.web.User;

import com.example.domain.Project;
import com.example.domain.TaskType;
import com.example.domain.User;
import com.example.domain.UserRole;
import com.example.ejb.EjbProjectRepository;
import com.example.ejb.EjbUserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
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

@SessionScoped
@Named("userBean")
public class UserBean implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    private LoginBean loginBean; // Inject LoginBean
    @Inject
    EjbUserRepository userRepository;

    private List<User> users;
    private UserForm form;
    private int currentPage = 1;
    private int itemsPerPage = 10;

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public FacesContext getFacesContext() {
        return facesContext;
    }

    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserForm getForm() {
        return form;
    }

    public void setForm(UserForm form) {
        this.form = form;
    }

    @PostConstruct
    public void init() {
        this.form = new UserForm();
        loadUsers();

    }

    public void loadUsers() {
        this.users = userRepository.findAll();
    }
        public List<UserRole> getUserRole() {
        return Arrays.asList(UserRole.values());
    }
    public void loadUserDetails() {
        this.currentUser = loginBean.getCurrentUser();
        // Load other user details such as email, phone, etc.
    }

    public void saveProject() {


        if (this.form.getId() == null) {
            userRepository.save(new User(form.getUsername(), form.getPassword(), UserRole.valueOf(form.getRole())));
        } else {
            User existing = userRepository.findById(this.form.getId());
            existing.setUsername(this.form.getUsername());
            existing.setPassword(this.form.getPassword());
            existing.setRole(UserRole.valueOf(this.form.getRole()));
            userRepository.save(existing);
        }
        loadUsers();
        this.form = new UserForm();
        facesContext.addMessage(null, new FacesMessage("uSER was saved successfully."));
    }

    public void updateProject() {
        User current = userRepository.findById(loginBean.getCurrentUser().getId());
        current.setContactDetails(this.form.getContactDetails());

            userRepository.save(current);
        facesContext.addMessage(null, new FacesMessage("PROFILE was UPDATED successfully."));
    }



    public void editProject(UUID id) {
        User user = userRepository.findById(id);
        this.form = new UserForm(user.getId(), user.getUsername(), user.getPassword(),user.getRole().toString());
    }

    public void deleteProject(UUID id) {
        userRepository.deleteById(id);
        loadUsers();
        facesContext.addMessage(null, new FacesMessage("Project was deleted successfully."));
    }
}