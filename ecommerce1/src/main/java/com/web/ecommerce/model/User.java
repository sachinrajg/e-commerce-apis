package com.web.ecommerce.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.EnumType;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;

    // @Enumerated(EnumType.STRING)
    // private Role role;

    private int role_id;

    public User() {
    }

    public User(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role_id = role_id;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // public Role getRole() {
    //     return role;
    // }

    // public void setRole(Role role) {
    //     this.role = role;
    // }

    // public enum Role {
    //     ADMIN, BUYER, SELLER;
    // }

    public void setRoleId(int role_id){
        this.role_id=role_id;
    }

    public int getRoleId(){
        return role_id;
    }
}
