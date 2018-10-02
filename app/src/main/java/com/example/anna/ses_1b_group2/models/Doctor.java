package com.example.anna.ses_1b_group2.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Doctor implements Serializable{


    public String user_id;
    private String email;
    private String username;
    private String medical_field;

    public Doctor(String user_id, String email, String username, String medical_field) {
        this.user_id = user_id;
        this.email = email;
        this.username = username;
        this.medical_field = medical_field;
    }

    public Doctor(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMedical_field() {
        return medical_field;
    }

    public void setMedical_field(String medical_field) {
        this.medical_field = medical_field;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", medical_field='" + medical_field + '\'' +
                '}';
    }
}
