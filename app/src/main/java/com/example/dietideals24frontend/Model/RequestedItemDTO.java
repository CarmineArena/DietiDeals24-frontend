package com.example.dietideals24frontend.Model;

import java.io.Serializable;

/* [CLASS DESCRIPTION]
    - This object is used to make a post request to the server asking to save/register an Item inside the database.
    - The request occurs in two steps:
        -- 1. First we send to the server the Item's related image (which is a byte[]). Sending the full Item was problematic.
        -- 2. We then send this object which is used by the Server to create the real Item and then it gets stored!
**/

public class RequestedItemDTO implements Serializable {
    private UserDTO user;
    private Integer itemId;
    private float basePrize;
    private String name, description, category;

    public RequestedItemDTO() {}

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}