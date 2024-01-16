package com.haotianxu.twitterlike.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@IdClass(RoleId.class)
@Table(name = "authorities")
public class Auth {
    @Id
    private String username;

    @Id
    private String authority;

    public Auth(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public Auth() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}


