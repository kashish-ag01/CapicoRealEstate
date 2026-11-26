package com.capico.property.dto.response;

public class PropertyOwnerDetailsResponse {

    private PropertyResponse property;

    private UserSummaryResponse owner;

    public PropertyOwnerDetailsResponse() {
    }

    public PropertyOwnerDetailsResponse(
            PropertyResponse property,
            UserSummaryResponse owner
    ) {
        this.property = property;
        this.owner = owner;
    }

    public PropertyResponse getProperty() {
        return property;
    }

    public void setProperty(PropertyResponse property) {
        this.property = property;
    }

    public UserSummaryResponse getOwner() {
        return owner;
    }

    public void setOwner(UserSummaryResponse owner) {
        this.owner = owner;
    }
}