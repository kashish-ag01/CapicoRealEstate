package com.capico.property.controller;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(
            PropertyService propertyService
    ) {
        this.propertyService = propertyService;
    }

    // ADD PROPERTY

    @PostMapping
    public ResponseEntity<PropertyResponse>
    createProperty(
            @Valid
            @RequestBody
            CreatePropertyRequest request
    ) {

        PropertyResponse savedProperty =
                propertyService.createProperty(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProperty);
    }
}
