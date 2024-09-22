package com.bekrenovr.ecommerce.keycloakserver.repository;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.keycloakserver.model.TokenType;
import com.bekrenovr.ecommerce.keycloakserver.model.entity.Token;
import com.bekrenovr.ecommerce.keycloakserver.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

import static com.bekrenovr.ecommerce.keycloakserver.exception.KeycloakApplicationExceptionReason.ACTIVATION_TOKEN_NOT_FOUND;

public class TokenRepository {
    public Optional<Token> findByValueAndType(String value, TokenType tokenType) {
        String query = "from Token t where t.value = :value and t.tokenType = :type";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            Token token = em.createQuery(query, Token.class)
                    .setParameter("value", value)
                    .setParameter("type", tokenType)
                    .getSingleResult();
            return Optional.of(token);
        } catch(NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Token> findByUsernameAndType(String username, TokenType tokenType) {
        String query = "from Token t where t.username = :username and t.tokenType = :type";
        try(EntityManager em = EntityManagerUtil.createEntityManager()){
            Token token = em.createQuery(query, Token.class)
                    .setParameter("username", username)
                    .setParameter("type", tokenType)
                    .getSingleResult();
            return Optional.of(token);
        } catch(NoResultException ex) {
            return Optional.empty();
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
            Token token = findByUsernameAndType(username, tokenType)
                    .orElseThrow(() -> new EcommerceApplicationException(ACTIVATION_TOKEN_NOT_FOUND, username));
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
}
