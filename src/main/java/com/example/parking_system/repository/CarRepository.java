package com.example.parking_system.repository;

import com.example.parking_system.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, String> {
    
}