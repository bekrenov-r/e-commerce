package com.bekrenovr.ecommerce.keycloakserver.dao;

import com.bekrenovr.ecommerce.keycloakserver.config.SecondaryDatasourceConfigProperties;
import com.bekrenovr.ecommerce.keycloakserver.model.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import org.keycloak.component.ComponentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bekrenovr.ecommerce.keycloakserver.providers.EcommerceUserStorageProviderConstants.*;

public class EcommerceUserDao {
    private static final String ALL_USERS_QUERY = "select * from users";
    private static final String ROLES_FOR_USER_QUERY = "select roles.role from roles join users on users.username = roles.username where users.username = ?";
    private static final String USER_BY_USERNAME_QUERY = "select * from users where users.username = ?";
    private static final String PASSWORD_BY_USERNAME_QUERY = "select password from users where username = ?";
    private static final String EXISTS_BY_USERNAME_QUERY = "select exists (select 1 from users where username = ?) as user_exists";
    private static final String ADD_USER = "insert into users values (?, ?)";
    private static final String ADD_ROLE = "insert into roles values (?, ?)";
    private ComponentModel componentModel;
    private SecondaryDatasourceConfigProperties dbConfigProperties;

    public EcommerceUserDao(ComponentModel componentModel) {
        this.componentModel = componentModel;
    }

    public EcommerceUserDao(SecondaryDatasourceConfigProperties secondaryDatasourceConfigProperties) {
        this.dbConfigProperties = secondaryDatasourceConfigProperties;
    }

    public List<EcommerceUser> getAllUsers() {
        try(Connection connection = getConnection(componentModel)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(ALL_USERS_QUERY);
            List<EcommerceUser> users = new ArrayList<>();
            while(rs.next()){
                String username = rs.getString("username");
                Set<Role> roles = getRolesForUser(username);
                EcommerceUser user = new EcommerceUser(rs.getString("username"), roles);
                users.add(user);
            }
            return users;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public EcommerceUser getUserByUsername(String username) {
        try(Connection connection = getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement(USER_BY_USERNAME_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new EcommerceUser(rs.getString("username"), getRolesForUser(username));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public String getPasswordByUsername(String username) {
        try(Connection connection = getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement(PASSWORD_BY_USERNAME_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public Set<Role> getRolesForUser(String username) {
        try(Connection connection = getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement(ROLES_FOR_USER_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            Set<Role> roles = new HashSet<>();
            while(rs.next()){
                roles.add(Role.valueOf(rs.getString("role")));
            }
            return roles;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public void addUser(String username, String password, Role role) {
        try(Connection connection = getConnection(dbConfigProperties)) {
            PreparedStatement addUserStatement = connection.prepareStatement(ADD_USER);
            addUserStatement.setString(1, username);
            addUserStatement.setString(2, password);
            addUserStatement.executeUpdate();
            addUserStatement.close();

            PreparedStatement addRoleStatement = connection.prepareStatement(ADD_ROLE);
            addRoleStatement.setString(1, username);
            addRoleStatement.setString(2, role.name());
            addRoleStatement.executeUpdate();
            addRoleStatement.close();
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public boolean existsByUsername(String username) {
        try(Connection connection = getConnection(dbConfigProperties)) {
            PreparedStatement ps = connection.prepareStatement(EXISTS_BY_USERNAME_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            boolean result = rs.getBoolean("user_exists");
            ps.close();
            return result;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    private Connection getConnection(ComponentModel config) {
        try {
            return DriverManager.getConnection(
                    config.get(CONFIG_KEY_JDBC_URL),
                    config.get(CONFIG_KEY_DB_USERNAME),
                    config.get(CONFIG_KEY_DB_PASSWORD)
            );
        } catch(SQLException ex) {
            throw new RuntimeException("Error connecting to database", ex);
        }
    }

    private Connection getConnection(SecondaryDatasourceConfigProperties properties) {
        try {
            return DriverManager.getConnection(
                    properties.getUrl(),
                    properties.getUsername(),
                    properties.getPassword()
            );
        } catch(SQLException ex) {
            throw new RuntimeException("Error connecting to database", ex);
        }
    }
}
