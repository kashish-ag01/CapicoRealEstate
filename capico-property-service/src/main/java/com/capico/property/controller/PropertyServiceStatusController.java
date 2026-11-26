package com.capico.property.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
public class PropertyServiceStatusController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getServiceStatus() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("service", "CAPICO-PROPERTY-SERVICE");
        response.put("status", "UP");
        response.put(
                "message",
                "Capico Property Service is running successfully"
        );
        response.put("port", 8082);
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}