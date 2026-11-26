package com.capico.propertyimage.repository;

import com.capico.propertyimage.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyImageRepository
        extends JpaRepository<PropertyImage, Long> {

    List<PropertyImage> findAllByPropertyId(Long propertyId);
}