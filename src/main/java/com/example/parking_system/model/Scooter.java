package com.example.parking_system.model;

import jakarta.persistence.Entity;

@Entity
public class Scooter extends Vehicle {
    public Scooter() {
        super();
    }

    public Scooter(String licensePlate) {
        super(licensePlate);
    }
}
