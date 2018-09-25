package com.example.anna.ses_1b_group2.models;

public class UserSettings {
    private User user;
    private UserProfile profile;
    private Doctor doctor;

    public UserSettings(User user, UserProfile profile, Doctor doctor) {
        this.user = user;
        this.profile = profile;
        this.doctor = doctor;
    }

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", profile=" + profile +
                ", doctor=" + doctor +
                '}';
    }
}
