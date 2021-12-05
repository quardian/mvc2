package com.inho.mvc2.domain;

import lombok.Data;

@Data
public class User {
    private String username;
    private int age;

    public User() {
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
