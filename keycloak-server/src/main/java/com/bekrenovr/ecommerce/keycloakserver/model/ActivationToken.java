package com.bekrenovr.ecommerce.keycloakserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activation_token")
@Data
@NoArgsConstructor
public class ActivationToken {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "token")
    private String token;

    public ActivationToken(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
