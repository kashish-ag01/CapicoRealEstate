package com.capico.property.service;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyAvailabilityRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
import com.capico.property.entity.Property;
import com.capico.property.enums.PropertyCategory;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.enums.PropertyType;
import com.capico.property.exception.DuplicatePropertyException;
import com.capico.property.exception.PropertyNotFoundException;
import com.capico.property.exception.ValidationException;
import com.capico.property.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(
            PropertyRepository propertyRepository
    ) {
        this.propertyRepository = propertyRepository;
    }

    // ADD PROPERTY
    public PropertyResponse createProperty(
            CreatePropertyRequest request
    ) {
        validateTypeAndCategory(
                request.getPropertyType(),
                request.getPropertyCategory()
        );

        validateDuplicatePropertyForCreate(request);

        Property property = new Property();

        property.setPropertyName(request.getPropertyName());
        property.setPropertyType(request.getPropertyType());
        property.setPropertyCategory(request.getPropertyCategory());
        property.setPropertyDescription(request.getPropertyDescription());
        property.setPropertyPrice(request.getPropertyPrice());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setCountry(request.getCountry());
        property.setPostalCode(request.getPostalCode());
        property.setAvailableFrom(request.getAvailableFrom());
        property.setStatus(PropertyStatus.AVAILABLE);

        Property savedProperty =
                propertyRepository.save(property);

        return mapToResponse(savedProperty);
    }

    // VIEW ALL PROPERTIES
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {
        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // VIEW PROPERTY BY ID
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(
            Long propertyId
    ) {
        return mapToResponse(
                findPropertyById(propertyId)
        );
    }

    // UPDATE PROPERTY
    public PropertyResponse updateProperty(
            Long propertyId,
            UpdatePropertyRequest request
    ) {
        Property property =
                findPropertyById(propertyId);

        validateTypeAndCategory(
                request.getPropertyType(),
                request.getPropertyCategory()
        );

        validateDuplicatePropertyForUpdate(
                propertyId,
                request
        );

        property.setPropertyName(request.getPropertyName());
        property.setPropertyType(request.getPropertyType());
        property.setPropertyCategory(request.getPropertyCategory());
        property.setPropertyDescription(request.getPropertyDescription());
        property.setPropertyPrice(request.getPropertyPrice());
        property.setAreaSqFt(request.getAreaSqFt());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setCountry(request.getCountry());
        property.setPostalCode(request.getPostalCode());
        property.setAvailableFrom(request.getAvailableFrom());

        Property updatedProperty =
                propertyRepository.save(property);

        return mapToResponse(updatedProperty);
    }

    // DELETE PROPERTY
    public void deleteProperty(
            Long propertyId
    ) {
        Property property =
                findPropertyById(propertyId);

        propertyRepository.delete(property);
    }

    // MANAGE PROPERTY AVAILABILITY
    public PropertyResponse updatePropertyAvailability(
            Long propertyId,
            UpdatePropertyAvailabilityRequest request
    ) {
        Property property =
                findPropertyById(propertyId);

        property.setStatus(request.getStatus());
        property.setAvailableFrom(request.getAvailableFrom());

        Property updatedProperty =
                propertyRepository.save(property);

        return mapToResponse(updatedProperty);
    }

    private Property findPropertyById(
            Long propertyId
    ) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(
                        () -> new PropertyNotFoundException(
                                "Property not found with ID: "
                                        + propertyId
                        )
                );
    }

    private void validateDuplicatePropertyForCreate(
            CreatePropertyRequest request
    ) {
        boolean duplicate =
                propertyRepository
                        .existsByPropertyNameIgnoreCaseAndAddressIgnoreCaseAndCityIgnoreCaseAndPostalCode(
                                request.getPropertyName(),
                                request.getAddress(),
                                request.getCity(),
                                request.getPostalCode()
                        );

        if (duplicate) {
            throw new DuplicatePropertyException(
                    "A property with the same name, address, city and postal code already exists"
            );
        }
    }

    private void validateDuplicatePropertyForUpdate(
            Long propertyId,
            UpdatePropertyRequest request
    ) {
        boolean duplicate =
                propertyRepository
                        .existsByPropertyNameIgnoreCaseAndAddressIgnoreCaseAndCityIgnoreCaseAndPostalCodeAndPropertyIdNot(
                                request.getPropertyName(),
                                request.getAddress(),
                                request.getCity(),
                                request.getPostalCode(),
                                propertyId
                        );

        if (duplicate) {
            throw new DuplicatePropertyException(
                    "Another property with the same name, address, city and postal code already exists"
            );
        }
    }

    private void validateTypeAndCategory(
            PropertyType propertyType,
            PropertyCategory propertyCategory
    ) {
        boolean validResidentialCategory =
                propertyType == PropertyType.RESIDENTIAL
                        && (
                        propertyCategory == PropertyCategory.APARTMENT
                                || propertyCategory == PropertyCategory.VILLA
                );

        boolean validCommercialCategory =
                propertyType == PropertyType.COMMERCIAL
                        && (
                        propertyCategory == PropertyCategory.OFFICE
                                || propertyCategory == PropertyCategory.SHOP
                );

        if (!validResidentialCategory
                && !validCommercialCategory) {

            throw new ValidationException(
                    "Invalid property type and category combination. "
                            + "RESIDENTIAL supports APARTMENT or VILLA, "
                            + "while COMMERCIAL supports OFFICE or SHOP"
            );
        }
    }

    private PropertyResponse mapToResponse(
            Property property
    ) {
        PropertyResponse response =
                new PropertyResponse();

        response.setPropertyId(property.getPropertyId());
        response.setPropertyName(property.getPropertyName());
        response.setPropertyType(property.getPropertyType());
        response.setPropertyCategory(property.getPropertyCategory());
        response.setPropertyDescription(
                property.getPropertyDescription()
        );
        response.setPropertyPrice(property.getPropertyPrice());
        response.setAreaSqFt(property.getAreaSqFt());
        response.setAddress(property.getAddress());
        response.setCity(property.getCity());
        response.setState(property.getState());
        response.setCountry(property.getCountry());
        response.setPostalCode(property.getPostalCode());
        response.setAvailableFrom(property.getAvailableFrom());
        response.setStatus(property.getStatus());
        response.setCreatedDate(property.getCreatedDate());

        return response;
    }
}