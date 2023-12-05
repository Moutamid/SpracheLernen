package com.moutamid.sprachelernen.models;

public class UserModel {
    String ID, name, email, password, language, image;

    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password, String language, String image) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
