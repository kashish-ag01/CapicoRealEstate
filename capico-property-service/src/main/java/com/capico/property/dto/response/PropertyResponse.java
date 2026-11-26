package com.capico.property.dto.response;

import com.capico.property.enums.FurnishingStatus;
import com.capico.property.enums.ListingType;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.enums.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PropertyResponse {

    private Long propertyId;
    private Long ownerId;
    private String title;
    private String description;
    private PropertyType propertyType;
    private ListingType listingType;
    private BigDecimal price;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private Integer bedrooms;
    private Integer bathrooms;
    private BigDecimal areaSqFt;
    private FurnishingStatus furnishingStatus;
    private PropertyStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PropertyResponse(
            Long propertyId,
            Long ownerId,
            String title,
            String description,
            PropertyType propertyType,
            ListingType listingType,
            BigDecimal price,
            String address,
            String city,
            String state,
            String country,
            String pinCode,
            Integer bedrooms,
            Integer bathrooms,
            BigDecimal areaSqFt,
            FurnishingStatus furnishingStatus,
            PropertyStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.propertyId = propertyId;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.propertyType = propertyType;
        this.listingType = listingType;
        this.price = price;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.areaSqFt = areaSqFt;
        this.furnishingStatus = furnishingStatus;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public BigDecimal getAreaSqFt() {
        return areaSqFt;
    }

    public FurnishingStatus getFurnishingStatus() {
        return furnishingStatus;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}