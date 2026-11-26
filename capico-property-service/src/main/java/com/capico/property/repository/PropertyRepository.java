package com.capico.property.repository;

import com.capico.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository
        extends JpaRepository<Property, Long> {

    boolean existsByPropertyNameIgnoreCaseAndAddressIgnoreCaseAndCityIgnoreCaseAndPostalCode(
            String propertyName,
            String address,
            String city,
            String postalCode
    );

    boolean existsByPropertyNameIgnoreCaseAndAddressIgnoreCaseAndCityIgnoreCaseAndPostalCodeAndPropertyIdNot(
            String propertyName,
            String address,
            String city,
            String postalCode,
            Long propertyId
    );
}