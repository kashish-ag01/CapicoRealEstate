package com.capico.property.controller;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyAvailabilityRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
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
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid
            @RequestBody
            CreatePropertyRequest request
    ) {
        PropertyResponse savedProperty =
                propertyService.createProperty(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProperty);
    }

    // VIEW ALL PROPERTIES
    @GetMapping
    public ResponseEntity<List<PropertyResponse>>
    getAllProperties() {
        return ResponseEntity.ok(
                propertyService.getAllProperties()
        );
    }

    // VIEW PROPERTY BY ID
    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse>
    getPropertyById(
            @PathVariable Long propertyId
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertyById(propertyId)
        );
    }

    // UPDATE PROPERTY
    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyResponse>
    updateProperty(
            @PathVariable Long propertyId,
            @Valid
            @RequestBody
            UpdatePropertyRequest request
    ) {
        return ResponseEntity.ok(
                propertyService.updateProperty(
                        propertyId,
                        request
                )
        );
    }

    // DELETE PROPERTY
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deleteProperty(
            @PathVariable Long propertyId
    ) {
        propertyService.deleteProperty(propertyId);

        return ResponseEntity.noContent().build();
    }

    // MANAGE PROPERTY AVAILABILITY
    @PatchMapping("/{propertyId}/availability")
    public ResponseEntity<PropertyResponse>
    updatePropertyAvailability(
            @PathVariable Long propertyId,
            @Valid
            @RequestBody
            UpdatePropertyAvailabilityRequest request
    ) {
        return ResponseEntity.ok(
                propertyService.updatePropertyAvailability(
                        propertyId,
                        request
                )
        );
    }
}