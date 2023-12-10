package com.example.dietideals24frontend.Model;

import java.io.Serializable;

public class ItemDTO implements Serializable {
    private Integer itemId;
    private String name, description, category;
    private float basePrize;
    private byte[] image;
    private UserDTO user;
    private AuctionDTO auction;

    /* CONSTRUCTOR */

    public ItemDTO() {}

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AuctionDTO getAuction() {
        return auction;
    }

    public void setAuction(AuctionDTO auction) {
        this.auction = auction;
    }
}