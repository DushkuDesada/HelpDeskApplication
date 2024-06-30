package com.example.domain;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException (UUID id) {
        super("Project :" + id + " was not found.");
    }
}

