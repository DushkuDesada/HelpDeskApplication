package com.example.web.User;

import com.example.domain.UserRole;

import java.io.Serializable;
import java.util.UUID;

public class UserForm implements Serializable {
    private UUID Id;
    private String username;
    private String password;
    private String role;

    public UserForm(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    private String contactDetails;

        public UserForm(UUID id, String username, String password, String role) {
        this.Id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    // Default constructor
    public UserForm() {}

    // Parameterized constructor
    public UserForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
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
