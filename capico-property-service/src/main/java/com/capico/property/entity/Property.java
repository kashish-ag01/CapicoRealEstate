package com.capico.property.entity;

import com.capico.property.enums.FurnishingStatus;
import com.capico.property.enums.ListingType;
import com.capico.property.enums.PropertyStatus;
import com.capico.property.enums.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
                        name = "idx_properties_owner_id",
                        columnList = "owner_id"
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
    @NotNull(message = "Owner ID is required")
    @Min(value = 1, message = "Owner ID must be greater than 0")
    @Column(
            name = "owner_id",
            nullable = false
    )
    private Long ownerId;
    @NotBlank(message = "Property title is required")
    @Size(
            min = 5,
            max = 200,
            message = "Property title must be between 5 and 200 characters"
    )
    @Column(
            name = "title",
            nullable = false,
            length = 200
    )
    private String title;
    @NotBlank(message = "Property description is required")
    @Size(
            min = 10,
            max = 2000,
            message = "Description must be between 10 and 2000 characters"
    )
    @Column(
            name = "description",
            nullable = false,
            length = 2000
    )
    private String description;
    @NotNull(message = "Property type is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "property_type",
            nullable = false,
            length = 30
    )
    private PropertyType propertyType;
    @NotNull(message = "Listing type is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "listing_type",
            nullable = false,
            length = 20
    )
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
    @Column(
            name = "price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal price;
    @NotBlank(message = "Property address is required")
    @Size(
            min = 5,
            max = 300,
            message = "Address must be between 5 and 300 characters"
    )
    @Column(
            name = "address",
            nullable = false,
            length = 300
    )
    private String address;
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Column(
            name = "city",
            nullable = false,
            length = 100
    )
    private String city;
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    @Column(
            name = "state",
            nullable = false,
            length = 100
    )
    private String state;
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Column(
            name = "country",
            nullable = false,
            length = 100
    )
    private String country;
    @NotBlank(message = "PIN code is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "PIN code must be a valid 6-digit Indian PIN code"
    )
    @Column(
            name = "pin_code",
            nullable = false,
            length = 10
    )
    private String pinCode;
    @NotNull(message = "Number of bedrooms is required")
    @Min(value = 0, message = "Bedrooms cannot be negative")
    @Column(
            name = "bedrooms",
            nullable = false
    )
    private Integer bedrooms;
    @NotNull(message = "Number of bathrooms is required")
    @Min(value = 0, message = "Bathrooms cannot be negative")
    @Column(
            name = "bathrooms",
            nullable = false
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
    @Column(
            name = "area_sq_ft",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal areaSqFt;
    @NotNull(message = "Furnishing status is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "furnishing_status",
            nullable = false,
            length = 30
    )
    private FurnishingStatus furnishingStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private PropertyStatus status;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Property() {
    }

    @PrePersist
    public void setDefaultValuesBeforeSaving() {

        if (status == null) {
            status = PropertyStatus.AVAILABLE;
        }

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updateTimestamp() {
        updatedAt = LocalDateTime.now();
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

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

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}