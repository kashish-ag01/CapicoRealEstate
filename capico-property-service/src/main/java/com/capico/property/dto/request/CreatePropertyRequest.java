package com.capico.property.dto.request;

import com.capico.property.enums.FurnishingStatus;
import com.capico.property.enums.ListingType;
import com.capico.property.enums.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreatePropertyRequest {

    @NotBlank(message = "Property title is required")
    @Size(
            min = 5,
            max = 200,
            message = "Property title must be between 5 and 200 characters"
    )
    private String title;

    @NotBlank(message = "Property description is required")
    @Size(
            min = 10,
            max = 2000,
            message = "Description must be between 10 and 2000 characters"
    )
    private String description;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Listing type is required")
    private ListingType listingType;

    @NotNull(message = "Property price is required")
    @DecimalMin(
            value = "1.00",
            message = "Property price must be greater than 0"
    )
    @Digits(
            integer = 13,
            fraction = 2,
            message = "Property price must contain up to 13 integer digits and 2 decimal places"
    )
    private BigDecimal price;

    @NotBlank(message = "Property address is required")
    @Size(
            min = 5,
            max = 300,
            message = "Address must be between 5 and 300 characters"
    )
    private String address;

    @NotBlank(message = "City is required")
    @Size(
            max = 100,
            message = "City cannot exceed 100 characters"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(
            max = 100,
            message = "State cannot exceed 100 characters"
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(
            max = 100,
            message = "Country cannot exceed 100 characters"
    )
    private String country;

    @NotBlank(message = "PIN code is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "PIN code must be a valid 6-digit Indian PIN code"
    )
    private String pinCode;

    @NotNull(message = "Number of bedrooms is required")
    @Min(
            value = 0,
            message = "Bedrooms cannot be negative"
    )
    private Integer bedrooms;

    @NotNull(message = "Number of bathrooms is required")
    @Min(
            value = 0,
            message = "Bathrooms cannot be negative"
    )
    private Integer bathrooms;

    @NotNull(message = "Property area is required")
    @DecimalMin(
            value = "1.00",
            message = "Property area must be greater than 0"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Property area must contain up to 10 integer digits and 2 decimal places"
    )
    private BigDecimal areaSqFt;

    @NotNull(message = "Furnishing status is required")
    private FurnishingStatus furnishingStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public BigDecimal getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(BigDecimal areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public FurnishingStatus getFurnishingStatus() {
        return furnishingStatus;
    }

    public void setFurnishingStatus(
            FurnishingStatus furnishingStatus
    ) {
        this.furnishingStatus = furnishingStatus;
    }
}