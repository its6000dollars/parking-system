package com.example.parking_system.model;

public class ParkingLot {
    private static final int TOTAL_SCOOTER_SLOTS = 20;
    private static final int TOTAL_CAR_SLOTS = 10;

    public static int getTotalScooterSlots() {
        return TOTAL_SCOOTER_SLOTS;
    }

    public static int getTotalCarSlots() {
        return TOTAL_CAR_SLOTS;
    }
}