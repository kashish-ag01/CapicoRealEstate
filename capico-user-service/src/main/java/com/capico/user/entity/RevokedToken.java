package com.capico.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revoked_token_id")
    private Long revokedTokenId;

    @Column(
            name = "token_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String tokenId;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;

    @Column(
            name = "revoked_date",
            nullable = false
    )
    private LocalDateTime revokedDate;

    @Column(
            name = "expiry_date",
            nullable = false
    )
    private LocalDateTime expiryDate;

    public RevokedToken() {
    }

    public Long getRevokedTokenId() {
        return revokedTokenId;
    }

    public void setRevokedTokenId(Long revokedTokenId) {
        this.revokedTokenId = revokedTokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getRevokedDate() {
        return revokedDate;
    }

    public void setRevokedDate(LocalDateTime revokedDate) {
        this.revokedDate = revokedDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}