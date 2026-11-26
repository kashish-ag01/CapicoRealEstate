package com.capico.property.service;

import com.capico.property.dto.request.CreatePropertyRequest;
import com.capico.property.dto.request.UpdatePropertyRequest;
import com.capico.property.dto.response.PropertyResponse;
import com.capico.property.entity.Property;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.exception.PropertyNotFoundException;
import com.capico.property.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

        Property property = new Property();

        property.setPropertyName(
                request.getPropertyName()
        );

        property.setPropertyType(
                request.getPropertyType()
        );

        property.setPropertyCategory(
                request.getPropertyCategory()
        );

        property.setPropertyDescription(
                request.getPropertyDescription()
        );

        property.setPropertyPrice(
                request.getPropertyPrice()
        );

        property.setAreaSqFt(
                request.getAreaSqFt()
        );

        property.setAddress(
                request.getAddress()
        );

        property.setCity(
                request.getCity()
        );

        property.setState(
                request.getState()
        );

        property.setCountry(
                request.getCountry()
        );

        property.setPostalCode(
                request.getPostalCode()
        );

        property.setAvailableFrom(
                request.getAvailableFrom()
        );

        Property savedProperty =
                propertyRepository.save(property);

        return mapToResponse(savedProperty);
    }

    // VIEW ALL PROPERTIES

    public List<PropertyResponse> getAllProperties() {

        return propertyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // VIEW PROPERTY BY ID

    public PropertyResponse getPropertyById(
            Long id
    ) {

        Property property =
                findPropertyById(id);

        return mapToResponse(property);
    }

    // UPDATE PROPERTY

    public PropertyResponse updateProperty(
            Long id,
            UpdatePropertyRequest request
    ) {

        Property property =
                findPropertyById(id);

        property.setPropertyName(
                request.getPropertyName()
        );

        property.setPropertyType(
                request.getPropertyType()
        );

        property.setPropertyCategory(
                request.getPropertyCategory()
        );

        property.setPropertyDescription(
                request.getPropertyDescription()
        );

        property.setPropertyPrice(
                request.getPropertyPrice()
        );

        property.setAreaSqFt(
                request.getAreaSqFt()
        );

        property.setAddress(
                request.getAddress()
        );

        property.setCity(
                request.getCity()
        );

        property.setState(
                request.getState()
        );

        property.setCountry(
                request.getCountry()
        );

        property.setPostalCode(
                request.getPostalCode()
        );

        property.setAvailableFrom(
                request.getAvailableFrom()
        );

        property.setStatus(
                request.getStatus()
        );

        Property updatedProperty =
                propertyRepository.save(property);

        return mapToResponse(updatedProperty);
    }

    // DELETE PROPERTY

    public void deleteProperty(
            Long id
    ) {

        Property property =
                findPropertyById(id);

        propertyRepository.delete(property);
    }

    // SEARCH BY CITY

    public List<PropertyResponse>
    getPropertiesByCity(
            String city
    ) {

        return propertyRepository
                .findAllByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // SEARCH BY STATUS

    public List<PropertyResponse>
    getPropertiesByStatus(
            PropertyStatus status
    ) {

        return propertyRepository
                .findAllByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // FIND PROPERTY

    private Property findPropertyById(
            Long id
    ) {

        return propertyRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new PropertyNotFoundException(
                                        "Property not found with ID: " + id
                                )
                );
    }

    // ENTITY TO RESPONSE

    private PropertyResponse mapToResponse(
            Property property
    ) {

        PropertyResponse response =
                new PropertyResponse();

        response.setPropertyId(
                property.getPropertyId()
        );

        response.setPropertyName(
                property.getPropertyName()
        );

        response.setPropertyType(
                property.getPropertyType()
        );

        response.setPropertyCategory(
                property.getPropertyCategory()
        );

        response.setPropertyDescription(
                property.getPropertyDescription()
        );

        response.setPropertyPrice(
                property.getPropertyPrice()
        );

        response.setAreaSqFt(
                property.getAreaSqFt()
        );

        response.setAddress(
                property.getAddress()
        );

        response.setCity(
                property.getCity()
        );

        response.setState(
                property.getState()
        );

        response.setCountry(
                property.getCountry()
        );

        response.setPostalCode(
                property.getPostalCode()
        );

        response.setAvailableFrom(
                property.getAvailableFrom()
        );

        response.setStatus(
                property.getStatus()
        );

        response.setCreatedDate(
                property.getCreatedDate()
        );

        return response;
    }
}