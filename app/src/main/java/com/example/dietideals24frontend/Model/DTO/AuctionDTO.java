package com.example.dietideals24frontend.Model.DTO;

import java.io.Serializable;
import com.example.dietideals24frontend.Model.Type;

/* [CLASS DESCRIPTION]
    - This object is used to make a post request to the server asking to save/register an Auction inside the database.
**/

public class AuctionDTO implements Serializable {
    private int ownerId;
    private boolean active;
    private Type auctionType;     // dd/MM/yyyy HH:mm:ss
    private String expirationTime;
    private String expirationDate; // This corresponds to the java.sql.Data attribute stored inside the Db (The String makes easier data transfer)
    private float currentOfferValue;
    private ItemDTO itemDTO;
    private Integer auctionId, requestedItemId, amountOfTimeToReset;

    public AuctionDTO() {}

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }

    public Integer getRequestedItemId() {
        return requestedItemId;
    }

    public void setRequestedItemId(Integer requestedItemId) {
        this.requestedItemId = requestedItemId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Type getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(Type auctionType) {
        this.auctionType = auctionType;
    }

    public float getCurrentOfferValue() {
        return currentOfferValue;
    }

    public void setCurrentOfferValue(float currentOfferValue) {
        this.currentOfferValue = currentOfferValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setAmountOfTimeToReset(Integer timeToReset) {
        this.amountOfTimeToReset = timeToReset;
    }

    public int getAmountOfTimeToReset() {
        return amountOfTimeToReset;
    }

    public ItemDTO getRequestedItemDTO() {
        return itemDTO;
    }

    public void setRequestedItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }
}