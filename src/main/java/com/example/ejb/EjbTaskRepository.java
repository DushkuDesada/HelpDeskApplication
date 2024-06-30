package com.example.ejb;

import com.example.domain.*;
import com.example.rest.TaskResource;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
@TransactionAttribute
public class EjbTaskRepository extends EntityRepository<Task, UUID> {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Inject
    EjbProjectRepository projectRepository;

    @Inject
    EjbWorkingHourRepository workingHourRepository;

    public List<Task> findByProject(UUID projectId) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.where(cb.equal(root.get("project").get("id"), projectId));
        return this.entityManager.createQuery(query).getResultList();
    }

    public List<Task> findByUser(UUID userId) {
        return entityManager.createQuery("SELECT t FROM Task t WHERE t.assignedTo.id = :userId", Task.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Task> findByCustomer(UUID userId) {
        List<Project> projects = projectRepository.findByUser(userId);
        List<Task> allTasks = new ArrayList<>();

        // Fetch tasks for each project and add them to the allTasks list
        for (Project project : projects) {
            List<Task> tasks = findByProject(project.getId());
            allTasks.addAll(tasks);
        }

        return allTasks;
    }
    public List<WorkingHour> findWorkingHoursByCustomer(UUID userId) {
        List<Task> tasks = findByCustomer(userId);
        List<WorkingHour> allWorkingHours = new ArrayList<>();

        // Fetch working hours for each task and add them to the allWorkingHours list
        for (Task task : tasks) {
            List<WorkingHour> workingHours = workingHourRepository.findByTask(task.getId());
            allWorkingHours.addAll(workingHours);
        }

        return allWorkingHours;
    }

}

