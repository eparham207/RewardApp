package com.itccloud.RewardProject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "fan_id")
    private FanPreference fan;

    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FanPreference getFan() { return fan; }
    public void setFan(FanPreference fan) { this.fan = fan; }
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
}