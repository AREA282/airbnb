package com.application.airbnb.utils;

import com.application.airbnb.domain.model.Property;
import com.application.airbnb.application.dto.PropertyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
@Component
public interface PropertyMapper {

    PropertyMapper INSTANCE = Mappers.getMapper(PropertyMapper.class);

    @Mapping(source = "propertyId", target = "id")
    @Mapping(target = "startDate", source = "availableStartDate")
    @Mapping(target = "endDate", source = "availableEndDate")
    @Mapping(target = "deleted", source = "isDeleted")
    PropertyDTO propertyToPropertyDTO(Property property);

    @Mapping(source = "id", target = "propertyId")
    @Mapping(target = "availableStartDate", source = "startDate")
    @Mapping(target = "availableEndDate", source = "endDate")
    @Mapping(target = "isDeleted", source = "deleted")
    Property propertyDTOToProperty(PropertyDTO propertyDTO);

    public default List<PropertyDTO> propertiesToPropertyDTOs(List<Property> properties) {
        return properties.stream()
                .map(this::propertyToPropertyDTO)
                .collect(Collectors.toList());
    }
}
