package com.application.airbnb.domain.model;

import com.application.airbnb.application.dto.PropertyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private boolean isDeleted = false;

    private Date date;

    public boolean lessTo30(){
        Date currentDay = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDay);
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date numberOfDays = calendar.getTime();
        return this.date.after(numberOfDays);
    }

    public boolean isValidLocation(String location)
    {
        return List.of("Medellin", "Bogota", "Cali", "Cartagena").contains(location);
    }

    public boolean isPriceValidForLocation(PropertyDTO propertyDTO)
    {
        if ("Bogota".equals(propertyDTO.getLocation()) || "Cali".equals(propertyDTO.getLocation())) {
            return propertyDTO.getPrice() <= 2000000;
        }
        return false;
    }

}
