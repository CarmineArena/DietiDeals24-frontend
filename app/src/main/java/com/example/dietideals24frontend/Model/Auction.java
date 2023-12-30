package com.example.dietideals24frontend.Model;

import java.util.Set;
import java.sql.Date;
import java.sql.Time;
import java.io.Serializable;

public class Auction implements Serializable {
    private Integer auctionId;
    private int ownerId;
    private Item item;
    private boolean active;
    private Type auctionType;
    private Set<Offer> offers;
    private Date expirationDate;
    private Time expirationTime;
    private float currentOfferValue;

    /* CONSTRUCTOR */

    public Auction() {}

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }
}