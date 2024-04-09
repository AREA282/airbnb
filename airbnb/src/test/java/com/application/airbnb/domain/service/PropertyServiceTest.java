package com.application.airbnb.domain.service;

import com.application.airbnb.application.dto.PropertyDTO;
import com.application.airbnb.application.service.PropertyService;
import com.application.airbnb.domain.model.Property;
import com.application.airbnb.domain.repository.PropertyRepository;
import com.application.airbnb.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProperty_Success() {
        // Given
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setName("Test Property");
        propertyDTO.setLocation("Bogota");
        propertyDTO.setPicture("picture");
        propertyDTO.setPrice(2500000L);
        propertyDTO.setDeleted(false);
        propertyDTO.setAvailable(true);

        // Mock repository method
        when(propertyRepository.existsByName(anyString())).thenReturn(false);
        when(propertyRepository.save(any(Property.class))).thenAnswer(invocation -> {
            Property property = invocation.getArgument(0);
            property.setPropertyId(1L); // Set a property ID for the created property
            return property;
        });

        // When
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO);

        // Then
        Assertions.assertNotNull(createdProperty);
        Assertions.assertEquals("Test Property", createdProperty.getName());
        Assertions.assertEquals("Bogota", createdProperty.getLocation());
        Assertions.assertEquals(Long.valueOf(2500000L), createdProperty.getPrice());
        Assertions.assertFalse(createdProperty.isDeleted());
        Assertions.assertTrue(createdProperty.isAvailable());
    }

    @Test
    void testCreateProperty_WithExistingName() {
        // Given
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setName("Existing Property");
        // Mock repository method
        when(propertyRepository.existsByName(anyString())).thenReturn(true);

        // When / Then
        assertThrows(BusinessException.class, () -> propertyService.createProperty(propertyDTO));
    }

    @Test
    void testCreatePropertyWithExistingName() {
        // Given
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setName("Existing Property");
        when(propertyRepository.existsByName("Existing Property")).thenReturn(true);

        // When / Then
        assertThrows(BusinessException.class, () -> propertyService.createProperty(propertyDTO));
    }

    @Test
    void testListProperties() {
        // Given
        double minPrice = 1000.0;
        double maxPrice = 2000.0;
        List<Property> properties = new ArrayList<>();
        properties.add(new Property());
        when(propertyRepository.findAvailablePropertiesByPriceRange(minPrice, maxPrice)).thenReturn(properties);

        // When
        List<PropertyDTO> result = propertyService.listProperties(minPrice, maxPrice);

        // Then
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(properties.size(), result.size());
    }

    @Test
    void testListProperties_NoPropertiesFound() {
        // Given
        double minPrice = 1000.0;
        double maxPrice = 2000.0;
        List<Property> properties = new ArrayList<>();
        when(propertyRepository.findAvailablePropertiesByPriceRange(minPrice, maxPrice)).thenReturn(properties);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> propertyService.listProperties(minPrice, maxPrice));
        Assertions.assertEquals("No hay propiedades disponibles con el rango de precio especificado", exception.getMessage());
    }

    @Test
    void deleteProperty_PropertyExistsAndIsNotDeleted_LessTo30_ReturnsDeletedProperty() {
        // Given
        Long propertyId = 1L;
        Property property = new Property();
        property.setPropertyId(propertyId);
        property.setDeleted(false);

        when(propertyRepository.findById(propertyId)).thenReturn(property);
        when(property.lessTo30()).thenReturn(true);

        // When
        propertyService.deleteProperty(propertyId);

        // Then
        Assertions.assertTrue(property.isDeleted());
        verify(propertyRepository, times(1)).save(property);
    }


    @Test
    void testDeleteProperty_NonExistentProperty() {
        // Given
        Long propertyId = 1L;
        // Mock repository method
        when(propertyRepository.findById(propertyId)).thenReturn(null);

        // When / Then
        BusinessException exception = assertThrows(BusinessException.class, () -> propertyService.deleteProperty(propertyId));
        Assertions.assertEquals("La propiedad con el ID especificado no ha sido encontrada", exception.getMessage());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void editProperty_PropertyExistsAndIsAvailable_LocationAndPriceNotModified_ReturnsEditedPropertyDTO() throws BusinessException {
        // Given
        long propertyId = 1L;
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPropertyId(propertyId);
        propertyDTO.setLocation("Bogota");
        propertyDTO.setPrice(2000000L);

        Property existingProperty = new Property();
        existingProperty.setPropertyId(propertyId);
        existingProperty.setLocation("Bogota");
        existingProperty.setPrice(2000000L);

        when(propertyRepository.findById(propertyId)).thenReturn(existingProperty);
        when(propertyRepository.createNewOrUpdateProperty(existingProperty)).thenReturn(existingProperty);

        // When
        PropertyDTO editedPropertyDTO = propertyService.editProperty(propertyDTO);

        // Then
        Assertions.assertNotNull(editedPropertyDTO);
        Assertions.assertEquals(existingProperty.getLocation(), editedPropertyDTO.getLocation());
        Assertions.assertEquals(existingProperty.getPrice(), editedPropertyDTO.getPrice());
    }
}
