package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.keycloakserver.model.entity.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class EcommerceUserRepository {
    public List<EcommerceUser> getAll() {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery("from EcommerceUser", EcommerceUser.class)
                    .getResultList();
        } catch(NoResultException ex){
            return null;
        }
    }

    public Optional<EcommerceUser> getByUsername(String username) {
        String query = "from EcommerceUser u where u.username = :username";
        try(EntityManager em = EntityManagerUtil.createEntityManager()) {
            EcommerceUser user = em.createQuery(query, EcommerceUser.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch(NoResultException ex){
            return Optional.empty();
        }
    }

    public void create(EcommerceUser user) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    public boolean existsByUsername(String username) {
        String query = "select exists (select 1 from EcommerceUser u where u.username = :username)";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Boolean.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }
    }

    public EcommerceUser enable(EcommerceUser user) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()) {
            user.setEnabled(true);
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return user;
        }
    }

    public void changePassword(EcommerceUser user, String challengeResponse) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()) {
            em.getTransaction().begin();
            EcommerceUser eu = em.find(EcommerceUser.class, user.getUsername());
            eu.setPassword(challengeResponse);
            em.merge(eu);
            em.getTransaction().commit();
        }
    }
}
