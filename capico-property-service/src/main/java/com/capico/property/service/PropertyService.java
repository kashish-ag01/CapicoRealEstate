package com.capico.property.service;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
import com.capico.property.entity.Property;
import com.capico.property.enums.ListingType;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.exception.PropertyNotFoundException;
import com.capico.property.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import com.capico.property.client.UserClient;
import com.capico.property.dto.response.PropertyOwnerDetailsResponse;
import com.capico.property.dto.response.UserSummaryResponse;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserClient userClient;

    public PropertyService(
            PropertyRepository propertyRepository,
            UserClient userClient
    ) {
        this.propertyRepository = propertyRepository;
        this.userClient = userClient;
    }

    // CREATE
    public PropertyResponse createProperty(
            CreatePropertyRequest request,
            Long ownerId
    ) {
        Property property = new Property();

        property.setOwnerId(ownerId);
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
        property.setListingType(request.getListingType());
        property.setPrice(request.getPrice());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setCountry(request.getCountry());
        property.setPinCode(request.getPinCode());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setFurnishingStatus(
                request.getFurnishingStatus()
        );

        Property savedProperty =
                propertyRepository.save(property);

        return mapToResponse(savedProperty);
    }

    // READ ALL
    public List<PropertyResponse> getAllProperties() {

        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // READ BY ID
    public PropertyResponse getPropertyById(Long id) {

        Property property = findPropertyById(id);

        return mapToResponse(property);
    }

    // UPDATE
    public PropertyResponse updateProperty(
            Long id,
            UpdatePropertyRequest request,
            Long userId,
            String role
    ) {
        Property existingProperty = findPropertyById(id);

        validateOwnership(existingProperty, userId, role);

        // Existing setters same rahenge...

        existingProperty.setTitle(request.getTitle());
        existingProperty.setDescription(
                request.getDescription()
        );
        existingProperty.setPropertyType(
                request.getPropertyType()
        );
        existingProperty.setListingType(
                request.getListingType()
        );
        existingProperty.setPrice(request.getPrice());
        existingProperty.setAddress(request.getAddress());
        existingProperty.setCity(request.getCity());
        existingProperty.setState(request.getState());
        existingProperty.setCountry(request.getCountry());
        existingProperty.setPinCode(request.getPinCode());
        existingProperty.setBedrooms(request.getBedrooms());
        existingProperty.setBathrooms(
                request.getBathrooms()
        );
        existingProperty.setAreaSqFt(
                request.getAreaSqFt()
        );
        existingProperty.setFurnishingStatus(
                request.getFurnishingStatus()
        );
        existingProperty.setStatus(request.getStatus());

        Property savedProperty =
                propertyRepository.save(existingProperty);

        return mapToResponse(savedProperty);
    }

    // DELETE
    public void deleteProperty(
            Long id,
            Long userId,
            String role
    ) {
        Property property = findPropertyById(id);

        validateOwnership(property, userId, role);

        propertyRepository.delete(property);
    }

    // SEARCH BY CITY
    public List<PropertyResponse> getPropertiesByCity(
            String city
    ) {
        return propertyRepository
                .findAllByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // SEARCH BY OWNER ID
    public List<PropertyResponse> getPropertiesByOwnerId(
            Long ownerId
    ) {
        return propertyRepository
                .findAllByOwnerId(ownerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // SEARCH BY STATUS
    public List<PropertyResponse> getPropertiesByStatus(
            PropertyStatus status
    ) {
        return propertyRepository
                .findAllByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // SEARCH BY LISTING TYPE
    public List<PropertyResponse> getPropertiesByListingType(
            ListingType listingType
    ) {
        return propertyRepository
                .findAllByListingType(listingType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // INTERNAL ENTITY LOOKUP
    private Property findPropertyById(Long id) {

        return propertyRepository.findById(id)
                .orElseThrow(() ->
                        new PropertyNotFoundException(
                                "Property not found with ID: " + id
                        )
                );
    }
    private void validateOwnership(
            Property property,
            Long userId,
            String role
    ) {
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        boolean isOwner = property.getOwnerId().equals(userId);

        if (!isAdmin && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to modify this property"
            );
        }
    }
    public PropertyOwnerDetailsResponse
    getPropertyOwnerDetails(Long propertyId) {

        Property property = findPropertyById(propertyId);

        UserSummaryResponse owner =
                userClient.getUserById(
                        property.getOwnerId()
                );

        PropertyOwnerDetailsResponse response =
                new PropertyOwnerDetailsResponse();

        response.setProperty(
                mapToResponse(property)
        );

        response.setOwner(owner);

        return response;
    }

    // ENTITY TO RESPONSE DTO
    private PropertyResponse mapToResponse(
            Property property
    ) {
        return new PropertyResponse(
                property.getPropertyId(),
                property.getOwnerId(),
                property.getTitle(),
                property.getDescription(),
                property.getPropertyType(),
                property.getListingType(),
                property.getPrice(),
                property.getAddress(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getPinCode(),
                property.getBedrooms(),
                property.getBathrooms(),
                property.getAreaSqFt(),
                property.getFurnishingStatus(),
                property.getStatus(),
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }

}