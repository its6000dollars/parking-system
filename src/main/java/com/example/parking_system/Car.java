package com.example.parking_system;

import java.util.Date;

public class Car {
    private String licensePlate;
    private Date entryTime;
    private Date exitTime;

    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = new Date();
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }
}
