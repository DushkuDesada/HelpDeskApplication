package com.example.ejb;

import com.example.domain.User;
import com.example.domain.UserRole;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
@TransactionAttribute
public class EjbUserRepository extends EntityRepository<User, UUID> implements Serializable {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultStream()
                .findFirst();
    }

    public User findByUsername(String username) {
        List<User> resultList = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public List<User> findByCustomerRole() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", UserRole.CUSTOMER)
                .getResultList();
    }
}
