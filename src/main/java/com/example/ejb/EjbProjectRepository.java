package com.example.ejb;

import com.example.domain.Project;
import com.example.domain.Task;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute
public class EjbProjectRepository extends EntityRepository<Project, UUID> implements Serializable {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<Project> findByUser(UUID userId) {
        return entityManager.createQuery("SELECT t FROM Project t WHERE t.owner.id = :userId", Project.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
