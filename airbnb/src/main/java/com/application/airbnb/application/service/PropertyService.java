package com.application.airbnb.application.service;

import com.application.airbnb.domain.model.Property;
import com.application.airbnb.domain.repository.PropertyRepository;
import com.application.airbnb.application.dto.PropertyDTO;
import com.application.airbnb.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    @Autowired
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public PropertyDTO createProperty(PropertyDTO propertyDTO)
    {
        Property property = propertyDTO.toDomain();
        if(propertyRepository.existsByName(propertyDTO.getName()))
        {
            throw new BusinessException("Ya existe una propiedad registrada con el mismo nombre");
        }

        if (!property.isValidLocation(propertyDTO.getLocation()))
        {
            throw new BusinessException("La ubicación que estás tratando de registrar es completamente inválida.");
        }

        if (property.isPriceValidForLocation(propertyDTO))
        {
            throw new BusinessException("El precio de la propiedad no cumple con los requisitos");
        }

        Property newProperty = propertyDTO.toDomain();
        Property propertyCreated = propertyRepository.save(newProperty);
        return PropertyDTO.fromDomain(propertyCreated);
    }

    public List<PropertyDTO> listProperties(double minPrice, double maxPrice) {
        List<Property> properties = propertyRepository.findAvailablePropertiesByPriceRange(minPrice, maxPrice);
        List<PropertyDTO> propertyDTOs = PropertyDTO.crateFromDomainList(properties);

        if (!propertyDTOs.isEmpty()) {
            return propertyDTOs;
        } else {
            throw new BusinessException("No hay propiedades disponibles con el rango de precio especificado");
        }
    }

    public void deleteProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId);
        if (property == null)
        {
            throw new BusinessException("La propiedad con el ID especificado no ha sido encontrada");
        } else if (property.lessTo30())
        {
            property.setDeleted(true);
            propertyRepository.save(property);
        }
    }


    public PropertyDTO rentProperty(Long propertyId) throws BusinessException {
        Property property = propertyRepository.findById(propertyId);
        if (!property.isAvailable()) {
            throw new BusinessException("La propiedad ya está arrendada");
        }
        property.setAvailable(false);

        Property updatedProperty = propertyRepository.save(property);

        return PropertyDTO.fromDomain(updatedProperty);
    }

    public PropertyDTO editProperty(PropertyDTO propertyDTO) throws BusinessException
    {
        Property propertyToEdit = propertyDTO.toDomain();
        Property existingProperty = propertyRepository.findById(propertyToEdit.getPropertyId());
        if (Boolean.TRUE.equals(!existingProperty.isAvailable()) && !existingProperty.getLocation().equals(propertyToEdit.getLocation()))
        {
            throw new BusinessException("No se puede mdoificar la ubicación de una propiedad ya arrendada");
        }
        if (Boolean.TRUE.equals(!existingProperty.isAvailable()) && !existingProperty.getPrice().equals(propertyToEdit.getPrice()))
        {
            throw new BusinessException("No se puede modificar el precio de una propiedad que está arrendada");
        }
        propertyToEdit.isPriceValidForLocation(propertyDTO);
        Property editedProperty = propertyRepository.createNewOrUpdateProperty(propertyToEdit);
        return PropertyDTO.fromDomain(editedProperty);
    }

}
