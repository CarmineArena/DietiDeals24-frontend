package com.example.dietideals24frontend.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class OfferDTO implements Serializable {
    private Long offerId;
    private UserDTO user;
    private AuctionDTO auction;
    private float offer;
    private LocalDate offerDate;
    private LocalTime offerTime;

    /* CONSTRUCTOR */

    public OfferDTO() {
        this.offerDate = LocalDate.now();
        this.offerTime = LocalTime.now();
    }

    /* GETTERS AND SETTERS */

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
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

    public float getOffer() {
        return offer;
    }

    public void setOffer(float offer) {
        this.offer = offer;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public LocalTime getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(LocalTime offerTime) {
        this.offerTime = offerTime;
    }
}