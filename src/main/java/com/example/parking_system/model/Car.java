package com.example.parking_system.model;

import jakarta.persistence.Entity;

@Entity
public class Car extends Vehicle {
    public Car() {
        super();
    }

    public Car(String licensePlate) {
        super(licensePlate);
    }
}