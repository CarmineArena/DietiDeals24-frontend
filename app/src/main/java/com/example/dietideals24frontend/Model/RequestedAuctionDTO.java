package com.example.dietideals24frontend.Model;

import java.sql.Time;
import java.io.Serializable;

/* [CLASS DESCRIPTION]
    - This object is used to make a post request to the server asking to save/register an Auction inside the database.
**/

public class RequestedAuctionDTO implements Serializable {
    private int ownerId;
    private boolean active;
    private Type auctionType;
    private Time expirationTime;
    private String expirationDate; // This corresponds to the java.sql.Data attribute stored inside the Db (The String makes easier data transfer)
    private float currentOfferValue;
    private RequestedItemDTO requestedItemDTO;
    private Integer auctionId, requestedItemId;

    public RequestedAuctionDTO() {}

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

    public Time getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    public RequestedItemDTO getRequestedItemDTO() {
        return requestedItemDTO;
    }

    public void setRequestedItemDTO(RequestedItemDTO requestedItemDTO) {
        this.requestedItemDTO = requestedItemDTO;
    }
}