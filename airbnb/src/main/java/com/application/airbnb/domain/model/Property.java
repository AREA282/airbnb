package com.application.airbnb.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    private Long propertyId;

    private String name;

    private String location;

    private String picture;

    private boolean isAvailable;

    private Long price;

    private boolean isDeleted;


}
