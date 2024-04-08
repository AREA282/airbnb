package com.application.airbnb.infrastructure.jpamodel;

import com.application.airbnb.domain.model.Property;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "properties")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JpaProperty {

    @Id
    @Column(name = "propertyId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "pictures")
    private String picture;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "price")
    private Long price;

    @Column(name = "is_deleted")
    private boolean isDeleted;

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
                .build();
    }

    public static JpaProperty fromDomain(Property property)
    {
        return JpaProperty.builder()
                .propertyId(property.getPropertyId())
                .name(property.getName())
                .location(property.getLocation())
                .picture(property.getPicture())
                .isAvailable(property.isAvailable())
                .price(property.getPrice())
                .isDeleted(property.isDeleted())
                .build();
    }

    public static List<JpaProperty> createFromDomainList(List<Property> list)
    {
        return list.stream()
                .map(JpaProperty::fromDomain)
                .toList();
    }

    public static List<Property> createToDomainList(List<JpaProperty> list)
    {
        return list.stream()
                .map(JpaProperty::toDomain)
                .toList();
    }
}
