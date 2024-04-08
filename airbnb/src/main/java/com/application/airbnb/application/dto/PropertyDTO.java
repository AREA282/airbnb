package com.application.airbnb.application.dto;

import com.application.airbnb.domain.model.Property;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {

    @NotNull(message = "Cannot be null.")
    private Long propertyId;

    @NotBlank(message = "Cannot be Blank")
    @NotNull(message = "Cannot be null")
    private String name;

    @NotBlank(message = "Cannot be Blank")
    @NotNull(message = "Cannot be null")
    private String location;

    @NotBlank(message = "Cannot be Blank")
    @NotNull(message = "Cannot be null")
    private String picture;

    @NotNull(message = "Cannot be null")
    private boolean isAvailable;

    @NotNull(message = "Cannot be null")
    @Min(value = 1, message = "El precio debe ser mayor que cero")
    private Long price;

    @NotNull(message = "Cannot be null")
    private boolean isDeleted = false;

    private Date date;

    public Property toDomain()
    {
        return Property.builder()
                .propertyId(this.propertyId)
                .name(this.name)
                .location(this.location)
                .picture(this.picture)
                .isAvailable(this.isAvailable)
                .price(this.price)
                .isDeleted(this.isDeleted)
                .date(this.date)
                .build();
    }

    public static PropertyDTO fromDomain(Property property)
    {
        return PropertyDTO.builder()
                .propertyId(property.getPropertyId())
                .name(property.getName())
                .location(property.getLocation())
                .picture(property.getPicture())
                .isAvailable(property.isAvailable())
                .price(property.getPrice())
                .isDeleted(property.isDeleted())
                .date(property.getDate())
                .build();
    }

    public static List<PropertyDTO> crateFromDomainList(List<Property> list)
    {
        return list.stream()
                .map(PropertyDTO::fromDomain)
                .toList();
    }

    public static List<Property> createToDomainList(List<PropertyDTO> list)
    {
        return list.stream()
                .map(PropertyDTO::toDomain)
                .toList();
    }

}
