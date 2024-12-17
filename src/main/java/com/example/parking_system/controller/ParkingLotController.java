package com.example.parking_system.controller;

import com.example.parking_system.model.Car;
import com.example.parking_system.model.ParkingLot;
import com.example.parking_system.model.Scooter;
import com.example.parking_system.repository.CarRepository;
import com.example.parking_system.repository.ScooterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class ParkingLotController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ScooterRepository scooterRepository;

    @GetMapping("/")
    public String payment(Model model) {
        updateAvailableSlots(model);
        model.addAttribute("result", null);
        return "index";
    }

    @PostMapping("/scooter")
    public String parkScooter(@RequestParam String licensePlate, Model model) {
        if (licensePlate.isEmpty()) {
            model.addAttribute("message", "請輸入車牌號碼！");
            updateAvailableSlots(model);
            return "index";
        }
        
        Scooter scooter = new Scooter(licensePlate);
        if (scooterRepository.existsById(licensePlate)) {
            model.addAttribute("scooterMessage", "車輛 " + licensePlate + " 已經停入車位");
        } else {
            long availableScooterSlots = ParkingLot.getTotalScooterSlots() - scooterRepository.count();
            if (availableScooterSlots > 0) {
                scooterRepository.save(scooter);
                model.addAttribute("scooterMessage", "車輛 " + licensePlate + " 已停入車位");
            }else {
                model.addAttribute("scooterMessage", "機車車位已滿！");
            }
        }
        updateAvailableSlots(model);
        return "index";
    }

    @PostMapping("/car")
    public String parkCar(@RequestParam String licensePlate, Model model) {
        if (licensePlate.isEmpty()) {
            model.addAttribute("carMessage", "請輸入車牌號碼！");
            updateAvailableSlots(model);
            return "index";
        }

        Car car = new Car(licensePlate);
        if (carRepository.existsById(licensePlate)) {
            model.addAttribute("carMessage", "車輛 " + licensePlate + " 已經停入車位");
        } else {
            long availableCarSlots = ParkingLot.getTotalCarSlots() - carRepository.count();
            if (availableCarSlots > 0) {
                carRepository.save(car);
                model.addAttribute("carMessage", "車輛 " + licensePlate + " 已停入車位");
            }else {
                model.addAttribute("carMessage", "汽車車位已滿！");
            }
        }
        updateAvailableSlots(model);
        return "index";
    }

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateFee(@RequestBody Map<String, String> requestBody, Model model) {
        String vehicleType = requestBody.get("vehicleType");
        String licensePlate = requestBody.get("licensePlate");

        if (licensePlate == null || licensePlate.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        if (vehicleType.equals("scooter")) {
            Scooter scooter = scooterRepository.findById(licensePlate).orElse(null);
            if (scooter == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Date now = new Date();
            long duration = now.getTime() - scooter.getEntryTime().getTime();
            long time = TimeUnit.MILLISECONDS.toMinutes(duration);
            double fee = 0;
            if (time >= 30) {
                fee = 30;
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String entryTimeFormatted = timeFormat.format(scooter.getEntryTime());
            String exitTimeFormatted = timeFormat.format(now);

            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
            String parkingTimeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            Map<String, Object> result = new HashMap<>();
            result.put("licensePlate", licensePlate);
            result.put("entryTime", entryTimeFormatted);
            result.put("exitTime", exitTimeFormatted);
            result.put("parkingTime", parkingTimeFormatted);
            result.put("fee", fee);

            return ResponseEntity.ok(result);
        } else {
            Car car = carRepository.findById(licensePlate).orElse(null);
            if (car == null) {
                model.addAttribute("message", "未找到車輛 " + licensePlate + "！");
                return ResponseEntity.badRequest().body(null);
            }

            Date now = new Date();
            long duration = now.getTime() - car.getEntryTime().getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            double fee = hours * 30;

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String entryTimeFormatted = timeFormat.format(car.getEntryTime());
            String exitTimeFormatted = timeFormat.format(now);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
            String parkingTimeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            Map<String, Object> result = new HashMap<>();
            result.put("licensePlate", licensePlate);
            result.put("entryTime", entryTimeFormatted);
            result.put("exitTime", exitTimeFormatted);
            result.put("parkingTime", parkingTimeFormatted);
            result.put("fee", fee);

            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> payFee(@RequestBody Map<String, String> requestBody) {
        String vehicleType = requestBody.get("vehicleType");
        String licensePlate = requestBody.get("licensePlate");

        if (licensePlate == null || licensePlate.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "請輸入正確車牌號碼！"));
        }

        if ("scooter".equals(vehicleType)) {
            scooterRepository.deleteById(licensePlate);
        } else if ("car".equals(vehicleType)) {
            carRepository.deleteById(licensePlate);
        }
        return ResponseEntity.ok(Map.of("message", "繳費成功"));
    }

    @GetMapping("/parkingLotList")
    public String parkingLotList(Model model) {
        model.addAttribute("scooterList", scooterRepository.findAll());
        model.addAttribute("carList", carRepository.findAll());
        return "parkingLotList";
    }

    private void updateAvailableSlots(Model model) {
        long availableScooterSlots = ParkingLot.getTotalScooterSlots() - scooterRepository.count();
        long availableCarSlots = ParkingLot.getTotalCarSlots() - carRepository.count();
        model.addAttribute("availableScooterSlots", availableScooterSlots);
        model.addAttribute("availableCarSlots", availableCarSlots);
    }
}