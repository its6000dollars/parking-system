package com.example.parking_system.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public abstract class Vehicle {
    @Id
    private String licensePlate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date entryTime;

    public Vehicle() {
        this.entryTime = new Date();
    }

    public Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = new Date();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Date getEntryTime() {
        return entryTime;
    }
}
