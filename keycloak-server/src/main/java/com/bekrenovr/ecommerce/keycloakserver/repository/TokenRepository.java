package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.keycloakserver.model.TokenType;
import com.bekrenovr.ecommerce.keycloakserver.model.entity.Token;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class TokenRepository {
    public Token findByValueAndType(String value, TokenType tokenType) {
        String query = "from Token t where t.value = :value and t.tokenType = :type";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Token.class)
                    .setParameter("value", value)
                    .setParameter("type", tokenType)
                    .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public Token findByUsernameAndType(String username, TokenType tokenType) {
        String query = "from Token t where t.username = :username and t.tokenType = :type";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Token.class)
                    .setParameter("username", username)
                    .setParameter("type", tokenType)
                    .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public void create(Token token) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            em.getTransaction().begin();
            em.persist(token);
            em.getTransaction().commit();
        }
    }

    public void removeByUsernameAndType(String username, TokenType tokenType) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            Token token = findByUsernameAndType(username, tokenType);
            em.getTransaction().begin();
            em.remove(em.merge(token));
            em.getTransaction().commit();
        }
    }

    public void removeAllByUsernameAndType(String username, TokenType tokenType) {
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            em.getTransaction().begin();
            String jpql = "delete from Token t where t.username = :username and t.tokenType = :type";
            em.createQuery(jpql)
                    .setParameter("username", username)
                    .setParameter("type", tokenType)
                    .executeUpdate();
            em.getTransaction().commit();
        }
    }

    public boolean existsByUsernameAndType(String username, TokenType tokenType) {
        String query = "select exists (select 1 from Token t where t.username = :username and t.tokenType = :type)";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Boolean.class)
                    .setParameter("username", username)
                    .setParameter("type", tokenType)
                    .getSingleResult();
        }
    }

    public boolean existsByValueAndType(String value, TokenType tokenType) {
        String query = "select exists (select 1 from Token t where t.value = :value and t.tokenType = :type)";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            return em.createQuery(query, Boolean.class)
                    .setParameter("value", value)
                    .setParameter("type", tokenType)
                    .getSingleResult();
        }
    }
}
