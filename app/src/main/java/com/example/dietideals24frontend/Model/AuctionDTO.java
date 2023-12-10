package com.example.dietideals24frontend.Model;

import java.io.Serializable;
import java.util.Set;
import java.sql.Date;
import java.sql.Time;

public class AuctionDTO implements Serializable {
    private Integer auctionId;
    private int ownerId;
    private Type auctionType;
    private float currentOfferValue;
    private Date expirationDate;
    private Time expirationTime;
    private ItemDTO item;
    private Set<OfferDTO> offers;

    /* CONSTRUCTOR */

    public AuctionDTO() {}

    /* GETTERS AND SETTERS */

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Time getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public Set<OfferDTO> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferDTO> offers) {
        this.offers = offers;
    }
}