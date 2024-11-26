package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.keycloakserver.model.entity.EcommerceUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EcommerceUserRepository {
    private final EntityManager em;

    public List<EcommerceUser> getAll() {
        try {
            return em.createQuery("from EcommerceUser", EcommerceUser.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Optional<EcommerceUser> getByUsername(String username) {
        String query = "from EcommerceUser u where u.username = :username";
        try {
            EcommerceUser user = em.createQuery(query, EcommerceUser.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public void create(EcommerceUser user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public boolean existsByUsername(String username) {
        String query = "select exists (select 1 from EcommerceUser u where u.username = :username)";
        return em.createQuery(query, Boolean.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public EcommerceUser enable(EcommerceUser user) {
        user.setEnabled(true);
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        return user;
    }

    public void changePassword(EcommerceUser user, String challengeResponse) {
        em.getTransaction().begin();
        EcommerceUser eu = em.find(EcommerceUser.class, user.getUsername());
        eu.setPassword(challengeResponse);
        em.merge(eu);
        em.getTransaction().commit();
    }
}
