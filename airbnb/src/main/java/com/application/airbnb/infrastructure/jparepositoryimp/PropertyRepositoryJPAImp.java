package com.application.airbnb.infrastructure.jparepositoryimp;

import com.application.airbnb.domain.model.Property;
import com.application.airbnb.domain.repository.PropertyRepository;
import com.application.airbnb.exception.DataBaseException;
import com.application.airbnb.infrastructure.jpamodel.JpaProperty;
import com.application.airbnb.infrastructure.jparepository.PropertyRepositoryJPA;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class PropertyRepositoryJPAImp implements PropertyRepository {

    private final PropertyRepositoryJPA propertyRepositoryJPA;
    @Override
    public Property findById(Long propertyId) {
        try
        {
            Optional<JpaProperty> propertyOptional = propertyRepositoryJPA.findById(propertyId);
            return propertyOptional.map(JpaProperty::toDomain).orElse(null);
        }catch(Exception exception)
        {
            log.error("Ocurrió un error al intentar encontrar la propiedad por id de la base de datos", exception);
            throw new DataBaseException("Ocurrió un error al intentar encontrar la propiedad por id de la BD");
        }
    }

    @Override
    public void deleteById(Long propertyId) {propertyRepositoryJPA.deleteById(propertyId);}

    @Override
    public Property save(Property property)
    {
        try
        {
            JpaProperty savedProperty = propertyRepositoryJPA.save(JpaProperty.fromDomain(property));
            return savedProperty.toDomain();
        }catch(Exception exception)
        {
            log.error("Ocurrió un error al intentar guardar la propiedad en la base de datos", exception);
            throw new DataBaseException("Ocurrió un error al intentar guardar la propiedad en la base de datos");
        }

    }

    @Override
    public boolean existsByName(String name) {return false;}

    @Override
    public List<Property> findAvailablePropertiesByPriceRange(double minPrice, double maxPrice)
    {
        try
        {
            List<JpaProperty> list = propertyRepositoryJPA.findAvailablePropertiesByPriceRange(minPrice, maxPrice);
            return JpaProperty.createToDomainList(list);
        }catch (Exception exception)
        {
            log.error("Ocurrió un error al intentar consultar las propiedades", exception);
            throw new DataBaseException("Ocurrió un error al intentar consultar las pripiedades");
        }

    }

    @Override
    public Property createNewOrUpdateProperty(Property property) {
        try
        {
            JpaProperty propertyToSave = propertyRepositoryJPA.save(JpaProperty.fromDomain(property));
            return propertyToSave.toDomain();
        }catch (Exception exception)
        {
            log.error("Ocurrió un error al intentar guardar la propiedad", exception);
            throw new DataBaseException("Ocurrió un error al intentar guardar la propiedad");
        }
    }
}
