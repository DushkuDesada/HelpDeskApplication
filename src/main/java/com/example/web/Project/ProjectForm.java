package com.example.web.Project;

import com.example.domain.User;
import java.util.UUID;

public class ProjectForm {
    private UUID id;
    private String name;
    private String ownerId; // Change the type to UUID

    // Default constructor
    public ProjectForm() {}

    // Parameterized constructor
    public ProjectForm(UUID id, String name, String ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }



    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
