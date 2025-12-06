package com.itccloud.RewardProject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fan_preferences")

public class FanPreference {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String occupation; // e.g., STU, EDU, MLT, OTH
    private String preferredStand; // e.g., North, South, East, West
    private LocalDateTime reservationTime;

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getPreferredStand() { return preferredStand; }
    public void setPreferredStand(String preferredStand) { this.preferredStand = preferredStand; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }
}

