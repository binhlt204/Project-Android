package com.example.tlucontact.models;

public class User {
    public String userId, fullName, email, role;

    public User() {}

    public User(String userId, String fullName, String email, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
}

