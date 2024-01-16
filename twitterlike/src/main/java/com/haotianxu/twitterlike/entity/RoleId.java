package com.haotianxu.twitterlike.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleId implements Serializable {
    private String username;
    private String authority;

    // Constructors, equals, and hashCode

    public RoleId(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public RoleId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleId roleId = (RoleId) o;
        return Objects.equals(getUsername(), roleId.getUsername()) && Objects.equals(getAuthority(), roleId.getAuthority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthority());
    }
}
