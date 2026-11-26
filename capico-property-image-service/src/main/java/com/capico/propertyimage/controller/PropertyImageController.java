package com.capico.propertyimage.controller;

import com.capico.propertyimage.dto.response.PropertyImageResponse;
import com.capico.propertyimage.service.PropertyImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/property-images")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(
            PropertyImageService propertyImageService
    ) {
        this.propertyImageService = propertyImageService;
    }

    // UPLOAD IMAGE
    @PostMapping("/{propertyId}")
    public ResponseEntity<PropertyImageResponse> uploadImage(
            @PathVariable Long propertyId,
            @RequestParam("file") MultipartFile file
    ) {
        PropertyImageResponse response =
                propertyImageService.uploadImage(
                        propertyId,
                        file
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET IMAGE METADATA BY IMAGE ID
    @GetMapping("/{imageId}")
    public ResponseEntity<PropertyImageResponse> getImageById(
            @PathVariable Long imageId
    ) {
        return ResponseEntity.ok(
                propertyImageService.getImageById(imageId)
        );
    }

    // GET ALL IMAGES OF A PROPERTY
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyImageResponse>>
    getImagesByPropertyId(
            @PathVariable Long propertyId
    ) {
        return ResponseEntity.ok(
                propertyImageService
                        .getImagesByPropertyId(propertyId)
        );
    }

    // DELETE IMAGE
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long imageId
    ) {
        propertyImageService.deleteImage(imageId);

        return ResponseEntity.ok(
                "Property image deleted successfully"
        );
    }
}