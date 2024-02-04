package com.example.dietideals24frontend.Model;

import java.util.Set;
import java.io.Serializable;
import com.example.dietideals24frontend.Model.DTO.UserDTO;

public class User implements Serializable {
    private Integer userId;
    private String name, surname, email, password, bio, webSiteUrl;
    private Set<Item> items;
    private Set<Offer> offers;

    /* CONSTRUCTOR */

    public User() {}

    /* GETTERS AND SETTERS */

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public static User createUser(UserDTO userDTO) {
        User loggedInUser = new User();
        loggedInUser.setUserId(userDTO.getUserId());
        loggedInUser.setName(userDTO.getName());
        loggedInUser.setSurname(userDTO.getSurname());
        loggedInUser.setEmail(userDTO.getEmail());
        loggedInUser.setPassword(userDTO.getPassword());
        loggedInUser.setBio(userDTO.getBio());
        loggedInUser.setWebSiteUrl(userDTO.getWebSiteUrl());

        return loggedInUser;
    }
}