package com.capico.property.dto.request;

import com.capico.property.enums.PropertyStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdatePropertyAvailabilityRequest {

    @NotNull(message = "Property status is required")
    private PropertyStatus status;

    @NotNull(message = "Available from date is required")
    private LocalDate availableFrom;

    public UpdatePropertyAvailabilityRequest() {
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalDate availableFrom) {
        this.availableFrom = availableFrom;
    }
}