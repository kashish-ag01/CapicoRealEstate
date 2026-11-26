package com.capico.property.controller;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
import com.capico.property.enums.ListingType;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.capico.property.security.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreatePropertyRequest request
    ) {
        PropertyResponse savedProperty =
                propertyService.createProperty(
                        request,
                        authenticatedUser.getUserId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProperty);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<PropertyResponse>>
    getAllProperties() {

        return ResponseEntity.ok(
                propertyService.getAllProperties()
        );
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertyById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdatePropertyRequest request
    ) {
        return ResponseEntity.ok(
                propertyService.updateProperty(
                        id,
                        request,
                        authenticatedUser.getUserId(),
                        authenticatedUser.getRole()
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        propertyService.deleteProperty(
                id,
                authenticatedUser.getUserId(),
                authenticatedUser.getRole()
        );

        return ResponseEntity.ok(
                "Property deleted successfully"
        );
    }

    // SEARCH BY CITY
    @GetMapping("/search/city")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByCity(
            @RequestParam String city
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertiesByCity(city)
        );
    }

    // SEARCH BY OWNER ID
    @GetMapping("/search/owner")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByOwnerId(
            @RequestParam Long ownerId
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertiesByOwnerId(ownerId)
        );
    }

    // SEARCH BY STATUS
    @GetMapping("/search/status")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByStatus(
            @RequestParam PropertyStatus status
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertiesByStatus(status)
        );
    }

    // SEARCH BY LISTING TYPE
    @GetMapping("/search/listing-type")
    public ResponseEntity<List<PropertyResponse>>
    getPropertiesByListingType(
            @RequestParam ListingType listingType
    ) {
        return ResponseEntity.ok(
                propertyService.getPropertiesByListingType(
                        listingType
                )
        );
    }
}