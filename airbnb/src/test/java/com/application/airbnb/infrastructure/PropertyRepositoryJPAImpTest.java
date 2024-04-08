package com.application.airbnb.infrastructure;
import com.application.airbnb.domain.model.Property;
import com.application.airbnb.exception.DataBaseException;
import com.application.airbnb.infrastructure.jpamodel.JpaProperty;
import com.application.airbnb.infrastructure.jparepository.PropertyRepositoryJPA;
import com.application.airbnb.infrastructure.jparepositoryimp.PropertyRepositoryJPAImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyRepositoryJPAImpTest {

    @Mock
    PropertyRepositoryJPA propertyRepositoryJPA;

    @InjectMocks
    PropertyRepositoryJPAImp propertyRepositoryJPAImp;

    @Test
    void findById_PropertyExists_ReturnsProperty() {
        // Given
        Long propertyId = 1L;
        Property property = new Property(); // Crear un objeto Property simulado

        // Utilizando thenAnswer para proporcionar una respuesta personalizada
        when(propertyRepositoryJPA.findById(propertyId)).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(propertyId)) {
                return Optional.of(property); // Devolver un Optional que contiene la propiedad simulada
            } else {
                return Optional.empty();
            }
        });

        // When
        Property result = propertyRepositoryJPAImp.findById(propertyId);

        // Then
        assertNotNull(result);
        assertEquals(property, result);
    }

    @Test
    void findById_PropertyNotExists_ReturnsNull() {
        // Given
        Long propertyId = 1L;
        when(propertyRepositoryJPA.findById(propertyId)).thenReturn(Optional.empty());

        // When
        Property property = propertyRepositoryJPAImp.findById(propertyId);

        // Then
        assertNull(property);
    }

    @Test
    void deleteById_ValidPropertyId_DeletesProperty() {
        // Given
        Long propertyId = 1L;

        // When
        propertyRepositoryJPAImp.deleteById(propertyId);

        // Then
        verify(propertyRepositoryJPA, times(1)).deleteById(propertyId);
    }

    @Test
    void save_ValidProperty_SavesProperty() {
        // Given
        Property property = new Property();
        JpaProperty jpaProperty = new JpaProperty();
        when(propertyRepositoryJPA.save(jpaProperty)).thenReturn(jpaProperty);

        // When
        Property savedProperty = propertyRepositoryJPAImp.save(property);

        // Then
        assertNotNull(savedProperty);
    }

    @Test
    void findAvailablePropertiesByPriceRange_ValidRange_ReturnsProperties() {
        // Given
        double minPrice = 1000;
        double maxPrice = 2000;
        JpaProperty jpaProperty1 = new JpaProperty();
        JpaProperty jpaProperty2 = new JpaProperty();
        List<JpaProperty> jpaProperties = Arrays.asList(jpaProperty1, jpaProperty2);
        when(propertyRepositoryJPA.findAvailablePropertiesByPriceRange(minPrice, maxPrice)).thenReturn(jpaProperties);

        // When
        List<Property> properties = propertyRepositoryJPAImp.findAvailablePropertiesByPriceRange(minPrice, maxPrice);

        // Then
        assertFalse(properties.isEmpty());
    }

    @Test
    void findAvailablePropertiesByPriceRange_ExceptionThrown_ThrowsDataBaseException() {
        // Given
        double minPrice = 1000;
        double maxPrice = 2000;
        when(propertyRepositoryJPA.findAvailablePropertiesByPriceRange(minPrice, maxPrice)).thenThrow(RuntimeException.class);

        // When/Then
        assertThrows(DataBaseException.class, () -> propertyRepositoryJPAImp.findAvailablePropertiesByPriceRange(minPrice, maxPrice));
    }
}


