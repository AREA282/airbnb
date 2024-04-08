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
        if(propertyRepository.existsByName(propertyDTO.getName()))
        {
            throw new BusinessException("Ya existe una propiedad registrada con el mismo nombre");
        }

        if (!isValidLocation(propertyDTO.getLocation()))
        {
            throw new BusinessException("La ubicación que estás tratando de registrar es completamente inválida.");
        }

        if (isPriceValidForLocation(propertyDTO))
        {
            throw new BusinessException("El precio de la propiedad no cumple con los requisitos");
        }

        Property newProperty = propertyDTO.toDomain();
        Property propertyCreated = propertyRepository.save(newProperty);
        return PropertyDTO.fromDomain(propertyCreated);
    }

    private boolean isValidLocation(String location)
    {
        return List.of("Medellin", "Bogota", "Cali", "Cartagena").contains(location);
    }

    private boolean isPriceValidForLocation(PropertyDTO propertyDTO)
    {
        if ("Bogota".equals(propertyDTO.getLocation()) || "Cali".equals(propertyDTO.getLocation())) {
            return propertyDTO.getPrice() <= 2000000;
        }
        return false;
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
        }

        property.setDeleted(true);
        propertyRepository.save(property);
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

    public PropertyDTO editProperty(PropertyDTO propertyDTO) throws BusinessException {
        // Obtener la propiedad existente desde la base de datos
        Property existingProperty = propertyRepository.findById(propertyDTO.getPropertyId());

        // Verificar si la propiedad existe
        if (existingProperty == null) {
            throw new BusinessException("La propiedad con el ID " + propertyDTO.getPropertyId() + " no se encontró");
        }

        // Verificar si la propiedad ya fue arrendada
        if (!existingProperty.isAvailable()) {
            throw new BusinessException("No se puede modificar una propiedad que ya fue arrendada");
        }

        // Validar que ningún campo esté vacío o nulo
        if (isNullOrEmpty(propertyDTO.getName()) || isNullOrEmpty(propertyDTO.getLocation()) ||
                propertyDTO.getPrice() == null || isNullOrEmpty(propertyDTO.getPicture())) {
            throw new BusinessException("Todos los campos son requeridos para modificar una propiedad");
        }

        // Validar que no se modifique la ubicación si la propiedad ya fue arrendada
        if (!existingProperty.getLocation().equals(propertyDTO.getLocation())) {
            throw new BusinessException("No se puede modificar la ubicación de una propiedad arrendada");
        }

        // Validar que no se modifique el precio si la propiedad ya fue arrendada
        if (!existingProperty.getPrice().equals(propertyDTO.getPrice())) {
            throw new BusinessException("No se puede modificar el precio de una propiedad arrendada");
        }

        // Validar que el precio no sea menor al estipulado por ubicación
        if (("Bogotá".equals(propertyDTO.getLocation()) || "Cali".equals(propertyDTO.getLocation())) &&
                propertyDTO.getPrice() < 2000000) {
            throw new BusinessException("El precio para propiedades en Bogotá o Cali no puede ser menor a 2'000.000");
        }

        // Actualizar los campos de la propiedad
        existingProperty.setName(propertyDTO.getName());
        existingProperty.setLocation(propertyDTO.getLocation());
        existingProperty.setPrice(propertyDTO.getPrice());
        existingProperty.setPicture(propertyDTO.getPicture());
        existingProperty.setAvailable(propertyDTO.isAvailable());

        Property updatedProperty = propertyRepository.save(existingProperty);

        return PropertyDTO.fromDomain(updatedProperty);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
