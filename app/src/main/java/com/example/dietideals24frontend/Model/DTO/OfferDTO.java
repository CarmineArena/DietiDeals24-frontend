package com.example.dietideals24frontend.Model.DTO;

import com.example.dietideals24frontend.Model.User;

import java.io.Serializable;
// import com.example.dietideals24frontend.Model.Auction;

public class OfferDTO implements Serializable {
    private Long offerId;
    private Integer auctionId;
    private User user;

    // private Auction auction; // We don't need the auction

    private float offer;
    private String offerDate;
    private String offerTime;

    /* GETTERS AND SETTERS */
    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getOffer() {
        return offer;
    }

    public void setOffer(float offer) {
        this.offer = offer;
    }

    public String getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(String offerDate) {
        this.offerDate = offerDate;
    }

    public String getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(String offerTime) {
        this.offerTime = offerTime;
    }
}