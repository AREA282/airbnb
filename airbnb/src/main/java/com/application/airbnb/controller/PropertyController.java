package com.application.airbnb.controller;

import com.application.airbnb.application.service.PropertyService;
import com.application.airbnb.application.dto.CustomResponseDTO;
import com.application.airbnb.application.dto.PropertyDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property")
public class PropertyController {

    private PropertyService propertyService;

    @PostMapping
    public ResponseEntity<Object> createProperty(@Validated @RequestBody PropertyDTO propertyDTO)
    {
        PropertyDTO savedProperty = propertyService.createProperty(propertyDTO);
        CustomResponseDTO res = new CustomResponseDTO("La propiedad ha sido creada satisfactoriamente!", HttpStatus.OK);
        res.setResponseObject(savedProperty);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> listProperties(
            @RequestParam("minPrice") double minPrice,
            @RequestParam("maxPrice") double maxPrice) {

            List<PropertyDTO> propertyDTOs = propertyService.listProperties(minPrice, maxPrice);
            return new ResponseEntity<>(propertyDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<Object> deleteProperty(@PathVariable Long propertyId)
    {
        propertyService.deleteProperty(propertyId);
        CustomResponseDTO res = new CustomResponseDTO("La propiedad has sido borrada satisfactoriamente", HttpStatus.OK);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/rent/{propertyId}")
    public ResponseEntity<Object> rentProperty(@Validated @PathVariable Long propertyId)
    {
        PropertyDTO rentedPropertyDTO = propertyService.rentProperty(propertyId);
        CustomResponseDTO res = new CustomResponseDTO("La propiedad fue rentada exitosamente!", HttpStatus.OK);
        res.setResponseObject(rentedPropertyDTO);
        return ResponseEntity.ok(res);
    }

    @PutMapping
    public ResponseEntity<Object> editProperty(@Valid @RequestBody PropertyDTO propertyDTO)
    {
        PropertyDTO propertyDTO1 = propertyService.editProperty(propertyDTO);
        CustomResponseDTO res = new CustomResponseDTO("La propiedad fue editada exitosamente!", HttpStatus.OK);
        res.setResponseObject(propertyDTO1);
        return ResponseEntity.ok(res);
    }
}
