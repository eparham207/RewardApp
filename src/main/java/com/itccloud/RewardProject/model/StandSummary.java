package com.itccloud.RewardProject.model;


public class StandSummary {
    private String standName;
    private int availableSeats;
    private double discountPrice;
    private int preferredSeats;

    // Getters
    public String getStandName() {
        return standName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public int getPreferredSeats() {
        return preferredSeats;
    }

    // Setters
    public void setStandName(String standName) {
        this.standName = standName;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void setPreferredSeats(int preferredSeats) {
        this.preferredSeats = preferredSeats;
    }
}
