package com.example.dietideals24frontend.Model;

import java.util.Set;
import java.sql.Date;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.example.dietideals24frontend.Model.DTO.AuctionDTO;

public class Auction implements Serializable {
    private Integer auctionId;
    private int ownerId, amountOfTimeToReset;
    private Item item;
    private boolean active;
    private Type auctionType;
    private Set<Offer> offers;
    private Date expirationDate = null;
    private LocalDateTime expirationTime = null;
    private float currentOfferValue, winningBid;

    /* CONSTRUCTOR */

    public Auction() {}

    public Auction(AuctionDTO auctionDTO, Item item) {
        this.auctionId         = auctionDTO.getAuctionId();
        this.ownerId           = auctionDTO.getOwnerId();
        this.item              = item;
        this.active            = auctionDTO.isActive();
        this.auctionType       = auctionDTO.getAuctionType();
        this.currentOfferValue = auctionDTO.getCurrentOfferValue();

        if (auctionDTO.getExpirationDate() != null) {
            this.expirationDate = Date.valueOf(auctionDTO.getExpirationDate());
        }

        if (auctionDTO.getExpirationTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            try {
                this.expirationTime = LocalDateTime.parse(auctionDTO.getExpirationTime(), formatter);
            } catch (DateTimeParseException e) {
                this.expirationTime = null;
            }
        }
    }

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

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setAmountOfTimeToReset(int timeToReset) {
        this.amountOfTimeToReset = timeToReset;
    }

    public int getAmountOfTimeToReset() {
        return amountOfTimeToReset;
    }

    public float getWinningBid() {
        return winningBid;
    }

    public void setWinningBid(float winningBid) {
        this.winningBid = winningBid;
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