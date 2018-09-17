package com.example.anna.ses_1b_group2.models;

public class UserProfile {
    private String full_name;
    private String gender;
    private String dob;
    private int height;
    private int weight;
    private String medical_condition;

    public UserProfile(String full_name, String gender, String dob, int height, int weight, String medical_condition) {
        this.full_name = full_name;
        this.gender = gender;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.medical_condition = medical_condition;
    }

    public UserProfile() {

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getMedical_condition() {
        return medical_condition;
    }

    public void setMedical_condition(String medical_condition) {
        this.medical_condition = medical_condition;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "full_name='" + full_name + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", medical_condition='" + medical_condition + '\'' +
                '}';
    }
}
