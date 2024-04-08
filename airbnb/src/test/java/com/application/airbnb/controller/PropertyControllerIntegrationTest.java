package com.application.airbnb.controller;

import com.application.airbnb.application.dto.CustomResponseDTO;
import com.application.airbnb.application.dto.PropertyDTO;
import com.application.airbnb.application.service.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PropertyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private PropertyController propertyController;
    @MockBean
    private PropertyService propertyService;

    @Test
    void createProperty() throws Exception {
        // Given
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setName("Test Property");
        propertyDTO.setLocation("Bogota");
        propertyDTO.setPicture("foto");
        propertyDTO.setPrice(1000000L);
        propertyDTO.setAvailable(true);

        PropertyDTO savedProperty = new PropertyDTO();
        savedProperty.setPropertyId(1L);
        savedProperty.setName("Test Property");
        savedProperty.setLocation("Bogota");
        savedProperty.setPicture("foto");
        savedProperty.setPrice(1000000L);
        savedProperty.setAvailable(true);

        when(propertyService.createProperty(any(PropertyDTO.class))).thenReturn(savedProperty);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propertyDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseBody = mvcResult.getResponse().getContentAsString();
        CustomResponseDTO response = new ObjectMapper().readValue(responseBody, CustomResponseDTO.class);

        Assertions.assertEquals("La propiedad ha sido creada satisfactoriamente!", response.getMessage());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(savedProperty, response.getResponseObject());
    }


    @Test
    void listProperties() throws Exception {
        when(propertyService.listProperties(0, 10000)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property?minPrice=0&maxPrice=10000"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void deleteProperty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/property/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void rentProperty() throws Exception {
        PropertyDTO rentedPropertyDTO = new PropertyDTO();
        rentedPropertyDTO.setName("Test Property");
        rentedPropertyDTO.setLocation("Bogota");
        rentedPropertyDTO.setPrice(1000000L);

        when(propertyService.rentProperty(1L)).thenReturn(rentedPropertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/property/rent/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.name").value("Test Property"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.location").value("Bogota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.price").value(1000000));
    }

    @Test
    void editProperty_Success() throws Exception {
        // Given
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPropertyId(1L);
        propertyDTO.setName("Updated Property");
        propertyDTO.setLocation("Bogota");
        propertyDTO.setPicture("updated_picture");
        propertyDTO.setAvailable(true);
        propertyDTO.setPrice(1500000L);
        propertyDTO.setDeleted(false);

        PropertyDTO updatedPropertyDTO = new PropertyDTO();
        updatedPropertyDTO.setPropertyId(1L);
        updatedPropertyDTO.setName("Updated Property");
        updatedPropertyDTO.setLocation("Bogota");
        updatedPropertyDTO.setPicture("updated_picture");
        updatedPropertyDTO.setAvailable(true);
        updatedPropertyDTO.setPrice(1500000L);
        updatedPropertyDTO.setDeleted(false);

        when(propertyService.editProperty(any(PropertyDTO.class))).thenReturn(updatedPropertyDTO);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("La propiedad fue editada exitosamente!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.name").value("Updated Property"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.location").value("Bogota"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseObject.price").value(1500000));
    }
}
