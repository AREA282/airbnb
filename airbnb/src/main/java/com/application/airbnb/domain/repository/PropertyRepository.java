package com.application.airbnb.domain.repository;

import com.application.airbnb.domain.model.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository {

    Property findById(Long propertyId);
    void deleteById(Long propertyId);
    Property save(Property property);
    boolean existsByName(String name);
    @Query("SELECT p FROM properties p WHERE p.isAvailable = true AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Property> findAvailablePropertiesByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

}
