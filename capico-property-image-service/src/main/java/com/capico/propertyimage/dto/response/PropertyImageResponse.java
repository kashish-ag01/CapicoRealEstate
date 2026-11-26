package com.capico.propertyimage.dto.response;

import com.capico.propertyimage.enums.ImageType;
import java.time.LocalDateTime;
@SuppressWarnings("all")
public class PropertyImageResponse {

    private Long imageId;
    private Long propertyId;
    private String imageUrl;
    private String imageName;
    private ImageType imageType;
    private LocalDateTime uploadDate;

    public PropertyImageResponse(
            Long imageId,
            Long propertyId,
            String imageUrl,
            String imageName,
            ImageType imageType,
            LocalDateTime uploadDate
    ) {
        this.imageId = imageId;
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.imageType = imageType;
        this.uploadDate = uploadDate;
    }

    public Long getImageId() {
        return imageId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
}