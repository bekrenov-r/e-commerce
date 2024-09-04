package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.keycloakserver.model.entity.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class EcommerceUserRepository {
    public List<EcommerceUser> getAll() {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery("from EcommerceUser", EcommerceUser.class)
                    .getResultList();
        } catch(NoResultException ex){
            return null;
        }
    }

    public EcommerceUser getByUsername(String username) {
        String query = "from EcommerceUser u where u.username = :username";
        try(EntityManager em = EntityManagerUtil.createEntityManager()) {
            return em.createQuery(query, EcommerceUser.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch(NoResultException ex){
            return null;
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

    public void enable(String username) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()) {
            EcommerceUser user = getByUsername(username);
            user.setEnabled(true);
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
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
