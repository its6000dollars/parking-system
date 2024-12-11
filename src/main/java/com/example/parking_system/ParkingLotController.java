package com.example.parking_system;

import com.example.parking_system.Car;
import com.example.parking_system.ParkingLot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParkingLotController {

    private ParkingLot parkingLot = new ParkingLot(100); // Example with 100 total slots

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }

    @PostMapping("/park")
    public String parkCar(@RequestParam String licensePlate, Model model) {
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
        if (parkingLot.removeCar(licensePlate)) {
            model.addAttribute("message", "車輛 " + licensePlate + " 已取出。");
        } else {
            model.addAttribute("message", "未找到車輛 " + licensePlate + "！");
        }
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }
}
