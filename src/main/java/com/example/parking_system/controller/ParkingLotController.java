package com.example.parking_system.controller;

import com.example.parking_system.model.Car;
import com.example.parking_system.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ParkingLotController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/")
    public String index(Model model) {
        long availableSlots = 10 - carRepository.count(); // Example with 10 total slots
        model.addAttribute("availableSlots", availableSlots);
        return "index";
    }

    @PostMapping("/park")
    public String parkCar(@RequestParam String licensePlate, Model model) {
        if (licensePlate.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼！");
            long availableSlots = 10 - carRepository.count();
            model.addAttribute("availableSlots", availableSlots);
            return "index";
        }
        Car car = new Car(licensePlate);
        if (carRepository.existsById(licensePlate)) {
            model.addAttribute("message", "車輛 " + licensePlate + " 已經停入車位。");
        } else {
            carRepository.save(car);
            model.addAttribute("message", "車輛 " + licensePlate + " 已停入車位。");
        }
        long availableSlots = 10 - carRepository.count();
        model.addAttribute("availableSlots", availableSlots);
        return "index";
    }

    @PostMapping("/remove")
    public String removeCar(@RequestParam String licensePlate, Model model) {
        if (licensePlate.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼！");
            long availableSlots = 10 - carRepository.count();
            model.addAttribute("availableSlots", availableSlots);
            return "index";
        }
        if (carRepository.existsById(licensePlate)) {
            carRepository.deleteById(licensePlate);
            model.addAttribute("message", "車輛 " + licensePlate + " 已取出。");
        } else {
            model.addAttribute("message", "未找到車輛 " + licensePlate + "！");
        }
        long availableSlots = 10 - carRepository.count();
        model.addAttribute("availableSlots", availableSlots);
        return "index";
    }

    @GetMapping("/cars")
    public String parkedCars(Model model) {
        List<Car> parkedCars = carRepository.findAll();
        model.addAttribute("parkedCars", parkedCars);
        return "cars";
    }
}
