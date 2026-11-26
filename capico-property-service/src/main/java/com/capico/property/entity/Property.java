package com.capico.property.entity;

import com.capico.property.enums.PropertyCategory;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "properties",
        indexes = {
                @Index(
                        name = "idx_properties_city",
                        columnList = "city"
                ),
                @Index(
                        name = "idx_properties_status",
                        columnList = "status"
                )
        }
)
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @NotBlank(message = "Property name is required")
    @Size(
            min = 5,
            max = 200,
            message = "Property name must be between 5 and 200 characters"
    )
    @Column(
            name = "property_name",
            nullable = false,
            length = 200
    )
    private String propertyName;

    @NotNull(message = "Property type is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "property_type",
            nullable = false
    )
    private PropertyType propertyType;

    @NotNull(message = "Property category is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "property_category",
            nullable = false
    )
    private PropertyCategory propertyCategory;

    @NotBlank(message = "Property description is required")
    @Size(
            min = 10,
            max = 2000,
            message = "Description must be between 10 and 2000 characters"
    )
    @Column(
            name = "property_description",
            nullable = false,
            length = 2000
    )
    private String propertyDescription;

    @NotNull(message = "Property price is required")
    @DecimalMin(
            value = "1.00",
            message = "Property price must be greater than 0"
    )
    @Column(
            name = "property_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal propertyPrice;

    @NotNull(message = "Property area is required")
    @DecimalMin(
            value = "1.00",
            message = "Property area must be greater than 0"
    )
    @Column(
            name = "area_sq_ft",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal areaSqFt;

    @NotBlank(message = "Address is required")
    @Column(
            name = "address",
            nullable = false,
            length = 300
    )
    private String address;

    @NotBlank(message = "City is required")
    @Column(
            name = "city",
            nullable = false
    )
    private String city;

    @NotBlank(message = "State is required")
    @Column(
            name = "state",
            nullable = false
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Column(
            name = "country",
            nullable = false
    )
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
    @Column(
            name = "postal_code",
            nullable = false,
            length = 20
    )
    private String postalCode;

    @NotNull(message = "Available from date is required")
    @Column(
            name = "available_from",
            nullable = false
    )
    private LocalDate availableFrom;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private PropertyStatus status;

    @Column(
            name = "created_date",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdDate;

    public Property() {
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = PropertyStatus.AVAILABLE;
        }

        createdDate = LocalDateTime.now();
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
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

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}