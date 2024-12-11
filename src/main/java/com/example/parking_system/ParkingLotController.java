package com.example.parking_system;

import com.example.parking_system.Car;
import com.example.parking_system.ParkingLot;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParkingLotController {

    private ParkingLot parkingLot = new ParkingLot(10); // Example with 100 total slots

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }

    @PostMapping("/park")
    public String parkCar(@RequestParam String licensePlate, Model model) {
        while (licensePlate.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼！");
            model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
            return "index";
        }
        Car car = new Car(licensePlate);
        if (parkingLot.parkCar(car)) {
            model.addAttribute("message", "車輛 " + licensePlate + " 已停入車位。");
        } else {
            model.addAttribute("message", "車位已滿！");
        }
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }

    @PostMapping("/remove")
    public String removeCar(@RequestParam String licensePlate, Model model) {
        if (licensePlate.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼！");
            model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
            return "index";
        }
        if (parkingLot.removeCar(licensePlate)) {
            model.addAttribute("message", "車輛 " + licensePlate + " 已取出。");
        } else {
            model.addAttribute("message", "未找到車輛 " + licensePlate + "！");
        }
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }

    @GetMapping("/cars")
    public String parkedCars(Model model) {
        List<Car> parkedCars = parkingLot.getParkedCars();
        model.addAttribute("parkedCars", parkedCars);
        return "cars";
    }
}
