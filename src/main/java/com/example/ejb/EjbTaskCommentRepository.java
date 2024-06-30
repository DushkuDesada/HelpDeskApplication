package com.example.ejb;

import com.example.domain.Project;
import com.example.domain.Task;
import com.example.domain.TaskComment;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@TransactionAttribute
public class EjbTaskCommentRepository extends EntityRepository<TaskComment, UUID> implements Serializable {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public TaskComment findLatestCommentByTaskId(UUID taskId) {
        TypedQuery<TaskComment> query = entityManager.createQuery("SELECT tc FROM TaskComment tc WHERE tc.task.id = :taskId ORDER BY tc.createdAt DESC", TaskComment.class);
        query.setParameter("taskId", taskId);
        query.setMaxResults(1);
        List<TaskComment> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

}
