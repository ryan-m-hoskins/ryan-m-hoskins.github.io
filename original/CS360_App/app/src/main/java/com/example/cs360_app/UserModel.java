package com.example.cs360_app;

public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    int id;

    public UserModel(String firstName, String lastName, String email, String password, String phoneNumber, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }
}
