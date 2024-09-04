package com.bekrenovr.ecommerce.keycloakserver.model.entity;

import com.bekrenovr.ecommerce.keycloakserver.model.TokenType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
public class Token {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "\"value\"")
    private String value;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    public Token(String username, String value, TokenType type) {
        this.username = username;
        this.value = value;
        this.tokenType = type;
    }
}
