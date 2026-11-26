package com.capico.propertyimage.service;

import com.capico.propertyimage.dto.response.PropertyImageResponse;
import com.capico.propertyimage.entity.PropertyImage;
import com.capico.propertyimage.enums.ImageType;
import com.capico.propertyimage.exception.FileStorageException;
import com.capico.propertyimage.exception.PropertyImageNotFoundException;
import com.capico.propertyimage.repository.PropertyImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.capico.propertyimage.client.PropertyClient;
import com.capico.propertyimage.dto.response.PropertyDetailsResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;
    private final Path uploadDirectory;
    private final PropertyClient propertyClient;

    public PropertyImageService(
            PropertyImageRepository propertyImageRepository,
            PropertyClient propertyClient,
            @Value("${file.upload-dir}") String uploadDirectory
    ) {
        this.propertyImageRepository = propertyImageRepository;
        this.propertyClient = propertyClient;
        this.uploadDirectory = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException exception) {
            throw new FileStorageException(
                    "Could not create image upload directory",
                    exception
            );
        }
    }

    // UPLOAD IMAGE
    public PropertyImageResponse uploadImage(
            Long propertyId,
            MultipartFile file
    ) {
        validatePropertyExists(propertyId);
        validateFile(file);

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {
            throw new FileStorageException(
                    "Image file name is missing"
            );
        }

        String cleanFileName = Paths.get(originalFileName)
                .getFileName()
                .toString();

        ImageType imageType = getImageType(cleanFileName);

        String generatedFileName =
                UUID.randomUUID() + "_" + cleanFileName;

        Path propertyDirectory =
                uploadDirectory.resolve(propertyId.toString());

        try {
            Files.createDirectories(propertyDirectory);

            Path targetLocation =
                    propertyDirectory.resolve(generatedFileName);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            PropertyImage propertyImage = new PropertyImage();

            propertyImage.setPropertyId(propertyId);
            propertyImage.setImageName(cleanFileName);
            propertyImage.setImageType(imageType);
            propertyImage.setImageUrl(
                    "/uploads/property-images/"
                            + propertyId
                            + "/"
                            + generatedFileName
            );

            PropertyImage savedImage =
                    propertyImageRepository.save(propertyImage);

            return mapToResponse(savedImage);

        } catch (IOException exception) {
            throw new FileStorageException(
                    "Could not store image file: "
                            + cleanFileName,
                    exception
            );
        }
    }

    // GET IMAGE BY ID
    public PropertyImageResponse getImageById(Long imageId) {
        return mapToResponse(
                findImageById(imageId)
        );
    }

    // GET ALL IMAGES FOR ONE PROPERTY
    public List<PropertyImageResponse> getImagesByPropertyId(
            Long propertyId
    ) {
        return propertyImageRepository
                .findAllByPropertyId(propertyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // DELETE IMAGE
    public void deleteImage(Long imageId) {

        PropertyImage propertyImage = findImageById(imageId);

        String storedFileName = Paths.get(
                propertyImage.getImageUrl()
        ).getFileName().toString();

        Path imagePath = uploadDirectory
                .resolve(propertyImage.getPropertyId().toString())
                .resolve(storedFileName)
                .normalize();

        if (!imagePath.startsWith(uploadDirectory)) {
            throw new FileStorageException(
                    "Invalid image file path"
            );
        }

        try {
            Files.deleteIfExists(imagePath);
            propertyImageRepository.delete(propertyImage);

        } catch (IOException exception) {
            throw new FileStorageException(
                    "Could not delete image file",
                    exception
            );
        }
    }

    // INTERNAL DATABASE LOOKUP
    private PropertyImage findImageById(Long imageId) {
        return propertyImageRepository.findById(imageId)
                .orElseThrow(() ->
                        new PropertyImageNotFoundException(
                                "Property image not found with ID: "
                                        + imageId
                        )
                );
    }

    // FILE VALIDATION
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException(
                    "Please select an image file"
            );
        }

        String contentType = file.getContentType();

        if (!"image/jpeg".equalsIgnoreCase(contentType)
                && !"image/png".equalsIgnoreCase(contentType)) {
            throw new FileStorageException(
                    "Only JPG, JPEG and PNG images are allowed"
            );
        }
    }

    // FILE EXTENSION TO ENUM
    private ImageType getImageType(String fileName) {
        String lowerCaseFileName = fileName.toLowerCase();

        if (lowerCaseFileName.endsWith(".png")) {
            return ImageType.PNG;
        }

        if (lowerCaseFileName.endsWith(".jpg")) {
            return ImageType.JPG;
        }

        if (lowerCaseFileName.endsWith(".jpeg")) {
            return ImageType.JPEG;
        }

        throw new FileStorageException(
                "Only JPG, JPEG and PNG images are allowed"
        );
    }

    // ENTITY TO RESPONSE DTO
    private PropertyImageResponse mapToResponse(
            PropertyImage propertyImage
    ) {
        return new PropertyImageResponse(
                propertyImage.getImageId(),
                propertyImage.getPropertyId(),
                propertyImage.getImageUrl(),
                propertyImage.getImageName(),
                propertyImage.getImageType(),
                propertyImage.getUploadDate()
        );
    }
    private PropertyDetailsResponse validatePropertyExists(Long propertyId) {

        PropertyDetailsResponse property =
                propertyClient.getPropertyById(propertyId);

        if (property == null || property.getPropertyId() == null) {
            throw new IllegalArgumentException(
                    "Property not found with ID: " + propertyId
            );
        }

        return property;
    }
}