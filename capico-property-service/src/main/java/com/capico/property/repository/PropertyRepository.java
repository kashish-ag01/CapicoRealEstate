package com.capico.property.repository;

import com.capico.property.entity.Property;
import com.capico.property.enums.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository
        extends JpaRepository<Property, Long> {

    List<Property> findAllByCityIgnoreCase(
            String city
    );

    List<Property> findAllByStatus(
            PropertyStatus status
    );
}