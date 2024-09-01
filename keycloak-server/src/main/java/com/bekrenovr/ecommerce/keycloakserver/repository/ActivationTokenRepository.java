package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.keycloakserver.model.ActivationToken;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;

public class ActivationTokenRepository {
    public ActivationToken findByToken(String token) {
        String query = "from ActivationToken a where a.token = :token";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, ActivationToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
        }
    }

    public ActivationToken findByUsername(String username) {
        String query = "from ActivationToken a where a.username = :username";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, ActivationToken.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
    }

    public void create(ActivationToken activationToken) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            em.getTransaction().begin();
            em.persist(activationToken);
            em.getTransaction().commit();
        }
    }

    public void removeByUsername(String username) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            ActivationToken activationToken = findByUsername(username);
            em.getTransaction().begin();
            em.remove(em.merge(activationToken));
            em.getTransaction().commit();
        }
    }

    public boolean existsByUsername(String username) {
        String query = "select exists (select 1 from ActivationToken a where a.username = :username)";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Boolean.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
    }

    public boolean existsByToken(String token) {
        String query = "select exists (select 1 from ActivationToken a where a.token = :token)";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Boolean.class)
                    .setParameter("token", token)
                    .getSingleResult();
        }
    }
}
