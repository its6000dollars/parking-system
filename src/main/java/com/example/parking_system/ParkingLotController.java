package com.example.parking_system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ParkingLotController {
    private ParkingLot parkingLot = new ParkingLot(10); // 初始化 10 個車位

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }
    @PostMapping("/park_and_remove")
    public String processCar(@RequestParam String licensePlate, 
                             @RequestParam String action, 
                             Model model) {
        if ("park".equals(action)) {
            Car car = new Car(licensePlate);
            if (parkingLot.parkCar(car)) {
                model.addAttribute("message", "車輛 " + licensePlate + " 已停入車位。");
            } else {
                model.addAttribute("message", "車位已滿！");
            }
        } else if ("remove".equals(action)) {
            if (parkingLot.removeCar(licensePlate)) {
                model.addAttribute("message", "車輛 " + licensePlate + " 已取出。");
            } else {
                model.addAttribute("message", "未找到車輛 " + licensePlate + "！");
            }
        }
        model.addAttribute("availableSlots", parkingLot.getAvailableSlots());
        return "index";
    }

   /*  
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
    }*/
/*
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
     */
    
}
