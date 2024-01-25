package com.example.dietideals24frontend.Model;

import java.io.Serializable;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.example.dietideals24frontend.Utility.ByteArrayAdapter;

public class Item implements Serializable {
    private Integer itemId;
    private String name, description, category;
    private float basePrize;

    @SerializedName("image")
    @JsonAdapter(ByteArrayAdapter.class)
    private byte[] image;
    private User user;
    private Auction auction;

    /* CONSTRUCTOR */

    public Item() {}

    /* GETTERS AND SETTERS */

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}