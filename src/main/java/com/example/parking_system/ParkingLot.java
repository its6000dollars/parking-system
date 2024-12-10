package com.example.parking_system;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private int totalSlots;
    private int availableSlots;
    private List<Car> parkedCars; // 用於追蹤已停的車輛

    public ParkingLot(int totalSlots) {
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots; // 初始可用車位等於總車位
        this.parkedCars = new ArrayList<>();
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public boolean parkCar(Car car) {
        if (availableSlots > 0) {
            parkedCars.add(car); // 將車輛加入已停車清單
            availableSlots--; // 減少可用車位
            return true; // 停車成功
        } else {
            return false; // 停車失敗，沒有空位
        }
    }

    public boolean removeCar(String licensePlate) {
        for (Car car : parkedCars) {
            if (car.getLicensePlate().equals(licensePlate)) {
                parkedCars.remove(car); // 移除車輛
                availableSlots++; // 增加可用車位
                return true; // 取車成功
            }
        }
        return false; // 取車失敗，找不到車輛
    }

    public List<Car> getParkedCars() {
        return parkedCars;
    }
}
