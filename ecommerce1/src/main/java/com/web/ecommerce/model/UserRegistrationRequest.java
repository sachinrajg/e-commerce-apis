package com.web.ecommerce.model;

import javax.persistence.Enumerated;

import com.web.ecommerce.model.User.Role;

import javax.persistence.EnumType;
public class UserRegistrationRequest {

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
