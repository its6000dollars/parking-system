package com.example.parking_system.controller;

import com.example.parking_system.model.Car;
import com.example.parking_system.model.Scooter;
import com.example.parking_system.repository.CarRepository;
import com.example.parking_system.repository.ScooterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParkingLotController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ScooterRepository scooterRepository;

    private static final int TOTAL_CAR_SLOTS = 10;
    private static final int TOTAL_SCOOTER_SLOTS = 5;

    @GetMapping("/")
    public String index(Model model) {
        long availableCarSlots = TOTAL_CAR_SLOTS - carRepository.count();
        long availableScooterSlots = TOTAL_SCOOTER_SLOTS - scooterRepository.count();
        model.addAttribute("availableCarSlots", availableCarSlots);
        model.addAttribute("availableScooterSlots", availableScooterSlots);
        return "index";
    }

    @PostMapping("/park")
    public String parkVehicle(
            @RequestParam String licensePlate,
            @RequestParam String vehicleType,
            Model model
    ) {
        if (licensePlate.isEmpty() || vehicleType.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼並選擇車輛類型！");
            updateSlots(model);
            return "index";
        }

        if (vehicleType.equals("Car")) {
            if (carRepository.existsById(licensePlate)) {
                model.addAttribute("message", "汽車 " + licensePlate + " 已經停入車位。");
            } else if (carRepository.count() >= TOTAL_CAR_SLOTS) {
                model.addAttribute("message", "汽車停車位已滿！");
            } else {
                carRepository.save(new Car(licensePlate));
                model.addAttribute("message", "汽車 " + licensePlate + " 已停入車位。");
            }
        } else if (vehicleType.equals("Scooter")) {
            if (scooterRepository.existsById(licensePlate)) {
                model.addAttribute("message", "摩托車 " + licensePlate + " 已經停入車位。");
            } else if (scooterRepository.count() >= TOTAL_SCOOTER_SLOTS) {
                model.addAttribute("message", "摩托車停車位已滿！");
            } else {
                scooterRepository.save(new Scooter(licensePlate));
                model.addAttribute("message", "摩托車 " + licensePlate + " 已停入車位。");
            }
        } else {
            model.addAttribute("message", "無效的車輛類型！");
        }

        updateSlots(model);
        return "index";
    }

    @PostMapping("/remove")
    public String removeVehicle(
            @RequestParam String licensePlate,
            @RequestParam String vehicleType,
            Model model
    ) {
        if (licensePlate.isEmpty() || vehicleType.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼並選擇車輛類型！");
            updateSlots(model);
            return "index";
        }

        if (vehicleType.equals("Car")) {
            if (carRepository.existsById(licensePlate)) {
                carRepository.deleteById(licensePlate);
                model.addAttribute("message", "汽車 " + licensePlate + " 已取出。");
            } else {
                model.addAttribute("message", "未找到汽車 " + licensePlate + "！");
            }
        } else if (vehicleType.equals("Scooter")) {
            if (scooterRepository.existsById(licensePlate)) {
                scooterRepository.deleteById(licensePlate);
                model.addAttribute("message", "摩托車 " + licensePlate + " 已取出。");
            } else {
                model.addAttribute("message", "未找到摩托車 " + licensePlate + "！");
            }
        } else {
            model.addAttribute("message", "無效的車輛類型！");
        }

        updateSlots(model);
        return "index";
    }

    private void updateSlots(Model model) {
        long availableCarSlots = TOTAL_CAR_SLOTS - carRepository.count();
        long availableScooterSlots = TOTAL_SCOOTER_SLOTS - scooterRepository.count();
        model.addAttribute("availableCarSlots", availableCarSlots);
        model.addAttribute("availableScooterSlots", availableScooterSlots);
    }
}

