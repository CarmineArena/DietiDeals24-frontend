package com.example.dietideals24frontend.utility;

import com.example.dietideals24frontend.modelDTO.User;

import java.io.Serializable;

public class RequestedItem implements Serializable {
    private String name, description, category;
    private float basePrize;
    private User user;

    public RequestedItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getBasePrize() {
        return basePrize;
    }

    public void setBasePrize(float basePrize) {
        this.basePrize = basePrize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}