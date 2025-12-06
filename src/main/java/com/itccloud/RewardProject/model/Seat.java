package com.itccloud.RewardProject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; // e.g., "E001"
    private String standName; // e.g., "East Stand"

    // No-arg constructor for JPA
    public Seat() {}

    // Constructor for our data initializer
    public Seat(String seatNumber, String standName) {
        this.seatNumber = seatNumber;
        this.standName = standName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getStandName() { return standName; }
    public void setStandName(String standName) { this.standName = standName; }
}