package com.example.anna.ses_1b_group2.models;

public class UserSettings {
    private User user;
    private UserProfile profile;

    public UserSettings(User user, UserProfile profile) {
        this.user = user;
        this.profile = profile;
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

    @Override
    public String toString() {
        return "UserSettings\n{" +
                "\nuser=" + user +
                ", \nprofile=" + profile +"\n" +
                '}';
    }
}
