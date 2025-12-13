package com.web.ecommerce.model;

public class JwtRequest {

    private String username;
    private String password;

    // Getters and setters (constructor can also be added if necessary)

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
}
