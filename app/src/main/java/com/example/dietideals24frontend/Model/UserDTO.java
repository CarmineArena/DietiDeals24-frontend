package com.example.dietideals24frontend.Model;

import java.io.Serializable;
import java.util.Set;

public class UserDTO implements Serializable {
    private Integer userId;
    private String name, surname, email, password;
    private Set<ItemDTO> items;
    private Set<OfferDTO> offers;

    /* CONSTRUCTOR */

    public UserDTO() {}

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

    public Set<ItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ItemDTO> items) {
        this.items = items;
    }

    public Set<OfferDTO> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferDTO> offers) {
        this.offers = offers;
    }
}