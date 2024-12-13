package com.example.parking_system.model;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private int totalCarSlots;
    private int totalScooterSlots;
    private int availableCarSlots;
    private int availableScooterSlots;
    private List<Car> parkedCars;
    private List<Scooter> parkedScooters;

    public ParkingLot(int totalCarSlots, int totalScooterSlots) {
        this.totalCarSlots = totalCarSlots;
        this.totalScooterSlots = totalScooterSlots;
        this.availableCarSlots = totalCarSlots;
        this.availableScooterSlots = totalScooterSlots;
        this.parkedCars = new ArrayList<>();
        this.parkedScooters = new ArrayList<>();
    }

    public int getAvailableCarSlots() {
        return availableCarSlots;
    }

    public int getAvailableScooterSlots() {
        return availableScooterSlots;
    }

    public boolean parkCar(Car car) {
        if (availableCarSlots > 0) {
            parkedCars.add(car);
            availableCarSlots--;
            return true;
        } else {
            return false;
        }
    }

    public boolean parkScooter(Scooter scooter) {
        if (availableScooterSlots > 0) {
            parkedScooters.add(scooter);
            availableScooterSlots--;
            return true;
        } else {
            return false;
        }
    }

    public boolean removeCar(String licensePlate) {
        for (Car car : parkedCars) {
            if (car.getLicensePlate().equals(licensePlate)) {
                parkedCars.remove(car);
                availableCarSlots++;
                return true;
            }
        }
        return false;
    }

    public boolean removeScooter(String licensePlate) {
        for (Scooter scooter : parkedScooters) {
            if (scooter.getLicensePlate().equals(licensePlate)) {
                parkedScooters.remove(scooter);
                availableScooterSlots++;
                return true;
            }
        }
        return false;
    }

    public List<Car> getParkedCars() {
        return parkedCars;
    }

    public List<Scooter> getParkedScooters() {
        return parkedScooters;
    }
}