package com.bekrenovr.ecommerce.keycloakserver.dao;

import com.bekrenovr.ecommerce.keycloakserver.config.SecondaryDatasourceConfigProperties;
import com.bekrenovr.ecommerce.keycloakserver.model.ActivationToken;
import com.bekrenovr.ecommerce.keycloakserver.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivationTokenDao {
    private static final String FIND_BY_TOKEN = "select * from activation_token where token = ?";
    private static final String SAVE_ACTIVATION_TOKEN = "insert into activation_token values (?, ?)";
    private static final String DELETE_ACTIVATION_TOKEN = "delete from activation_token where username = ?";
    private static final String TOKEN_EXISTS = "select exists (select 1 from activation_token where token = ?) as token_exists";

    private final SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties;

    public ActivationTokenDao(SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties) {
        this.secondaryDatasourceConfigProperties = secondaryDatasourceConfigProperties;
    }

    public ActivationToken findByToken(String token) {
        try (Connection connection = DbUtil.getConnection(secondaryDatasourceConfigProperties)) {
            PreparedStatement ps = connection.prepareStatement(FIND_BY_TOKEN);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ActivationToken result = new ActivationToken(
                    rs.getString("username"), rs.getString("token")
            );
            ps.close();
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public void save(ActivationToken activationToken) {
        try (Connection connection = DbUtil.getConnection(secondaryDatasourceConfigProperties)) {
            PreparedStatement ps = connection.prepareStatement(SAVE_ACTIVATION_TOKEN);
            ps.setString(1, activationToken.getUsername());
            ps.setString(2, activationToken.getToken());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public void delete(String username) {
        try (Connection connection = DbUtil.getConnection(secondaryDatasourceConfigProperties)) {
            PreparedStatement ps = connection.prepareStatement(DELETE_ACTIVATION_TOKEN);
            ps.setString(1, username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public boolean exists(String token) {
        try (Connection connection = DbUtil.getConnection(secondaryDatasourceConfigProperties)) {
            PreparedStatement ps = connection.prepareStatement(TOKEN_EXISTS);
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean result = rs.getBoolean("token_exists");
            ps.close();
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }
}