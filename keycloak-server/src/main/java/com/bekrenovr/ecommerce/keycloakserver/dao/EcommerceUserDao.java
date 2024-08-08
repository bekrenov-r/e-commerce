package com.bekrenovr.ecommerce.keycloakserver.dao;

import com.bekrenovr.ecommerce.keycloakserver.model.EcommerceUser;
import com.bekrenovr.ecommerce.keycloakserver.model.Role;
import com.bekrenovr.ecommerce.keycloakserver.util.DbUtil;
import lombok.NoArgsConstructor;
import org.keycloak.component.ComponentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class EcommerceUserDao {
    private static final String ALL_USERS_QUERY = "select * from users";
    private static final String ROLES_FOR_USER_QUERY = "select roles.role from roles join users on users.username = roles.username where users.username = ?";
    private static final String USER_BY_USERNAME_QUERY = "select * from users where users.username = ?";
    private static final String PASSWORD_BY_USERNAME_QUERY = "select password from users where username = ?";
    private static final String EXISTS_BY_USERNAME_QUERY = "select exists (select 1 from users where username = ?) as user_exists";
    private static final String ADD_USER = "insert into users values (?, ?, false)";
    private static final String ADD_ROLE = "insert into roles values (?, ?)";
    private static final String ENABLE_USER = "update users set enabled = true where username = ?";
    private ComponentModel componentModel;

    public EcommerceUserDao(ComponentModel componentModel) {
        this.componentModel = componentModel;
    }

    public List<EcommerceUser> getAllUsers() {
        try(Connection connection = DbUtil.getConnection(componentModel)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(ALL_USERS_QUERY);
            List<EcommerceUser> users = new ArrayList<>();
            while(rs.next()){
                String username = rs.getString("username");
                Set<Role> roles = getRolesForUser(username);
                EcommerceUser user = new EcommerceUser(
                        rs.getString("username"),
                        roles,
                        rs.getBoolean("enabled")
                );
                users.add(user);
            }
            return users;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public EcommerceUser getUserByUsername(String username) {
        try(Connection connection = DbUtil.getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement(USER_BY_USERNAME_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new EcommerceUser(
                        rs.getString("username"),
                        getRolesForUser(username),
                        rs.getBoolean("enabled")
                );
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public String getPasswordByUsername(String username) {
        try(Connection connection = DbUtil.getConnection(componentModel)) {
            PreparedStatement ps = connection.prepareStatement(PASSWORD_BY_USERNAME_QUERY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("password") : null;
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }

    public Set<Role> getRolesForUser(String username) {
        try(Connection connection = DbUtil.getConnection(componentModel)) {
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
        try(Connection connection = DbUtil.getConnection()) {
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
        try(Connection connection = DbUtil.getConnection()) {
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

    public void enableUser(String username) {
        try(Connection connection = DbUtil.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(ENABLE_USER);
            ps.setString(1, username);
            ps.executeUpdate();
            ps.close();
        } catch(SQLException ex) {
            throw new RuntimeException("Error executing SQL statement", ex);
        }
    }
}
