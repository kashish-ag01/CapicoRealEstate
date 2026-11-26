package com.capico.property.dto.request;

import com.capico.property.enums.PropertyCategory;
import com.capico.property.enums.PropertyType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdatePropertyRequest {

    @NotBlank(message = "Property name is required")
    @Size(
            min = 5,
            max = 200,
            message = "Property name must be between 5 and 200 characters"
    )
    private String propertyName;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotNull(message = "Property category is required")
    private PropertyCategory propertyCategory;

    @NotBlank(message = "Property description is required")
    @Size(
            min = 10,
            max = 2000,
            message = "Property description must be between 10 and 2000 characters"
    )
    private String propertyDescription;

    @NotNull(message = "Property price is required")
    @Positive(message = "Property price must be greater than zero")
    @Digits(
            integer = 13,
            fraction = 2,
            message = "Property price can contain up to 13 integer digits and 2 decimal places"
    )
    private BigDecimal propertyPrice;

    @NotNull(message = "Property area is required")
    @Positive(message = "Property area must be greater than zero")
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Property area can contain up to 10 integer digits and 2 decimal places"
    )
    private BigDecimal areaSqFt;

    @NotBlank(message = "Property address is required")
    @Size(
            min = 5,
            max = 300,
            message = "Address must be between 5 and 300 characters"
    )
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(
            min = 3,
            max = 20,
            message = "Postal code must be between 3 and 20 characters"
    )
    @Pattern(
            regexp = "^[A-Za-z0-9][A-Za-z0-9 -]*[A-Za-z0-9]$",
            message = "Postal code may contain letters, numbers, spaces and hyphens"
    )
    private String postalCode;

    @NotNull(message = "Available from date is required")
    private LocalDate availableFrom;

    public UpdatePropertyRequest() {
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public PropertyCategory getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(PropertyCategory propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public BigDecimal getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(BigDecimal propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public BigDecimal getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(BigDecimal areaSqFt) {
        this.areaSqFt = areaSqFt;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }
}