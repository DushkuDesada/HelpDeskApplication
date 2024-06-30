package com.example.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends AbstractEntity<UUID> {
    private String username;
    private String password;


    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "contact_details")
    @Nullable
    private String contactDetails;

    @Nullable
    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(@Nullable String contactDetails) {
        this.contactDetails = contactDetails;
    }
    @OneToMany(mappedBy = "user")
    private List<WorkingHour> workingHours;

    @OneToMany(mappedBy = "owner")
    private List<Project> projects;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password) {
        super();
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
