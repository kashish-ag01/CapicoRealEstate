package com.capico.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserServiceStatusController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServiceStatus() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("service", "CAPICO-USER-SERVICE");
        response.put("status", "UP");
        response.put("message", "Capico User Service is running successfully");
        response.put("port", 8081);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}