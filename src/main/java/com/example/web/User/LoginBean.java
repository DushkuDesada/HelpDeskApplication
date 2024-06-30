package com.example.web.User;

import com.example.domain.User;
import com.example.ejb.EjbUserRepository;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Optional;

@SessionScoped
@Named("loginBean")
public class LoginBean implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    EjbUserRepository userRepository;

    private String username;
    private String password;
    private User currentUser;

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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String login() {
        System.out.println(userRepository.findAll());
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            return "dashboard";
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password.", null));
            return null;
        }
    }

    public String logout() {
        currentUser = null;
        return "index.xhtml";
    }
}
