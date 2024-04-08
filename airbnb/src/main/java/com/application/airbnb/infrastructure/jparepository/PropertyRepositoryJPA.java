package com.application.airbnb.infrastructure.jparepository;


import com.application.airbnb.infrastructure.jpamodel.JpaProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepositoryJPA extends JpaRepository<JpaProperty, Long> {
    boolean existsByName(String name);
    @Query("SELECT p FROM JpaProperty p WHERE p.isAvailable = true AND p.price BETWEEN :minPrice AND :maxPrice")
    List<JpaProperty> findAvailablePropertiesByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

}
