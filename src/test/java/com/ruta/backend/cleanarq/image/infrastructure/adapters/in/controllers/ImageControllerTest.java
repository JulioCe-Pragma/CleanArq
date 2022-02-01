package com.ruta.backend.cleanarq.image.infrastructure.adapters.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruta.backend.cleanarq.errorhandling.application.ControllerAdvice;
import com.ruta.backend.cleanarq.image.application.usecase.ImageService;
import com.ruta.backend.cleanarq.image.infrastructure.dto.ImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class ImageControllerTest {

    private MockMvc mvc;
    private ImageDTO imageDTO;
    private JacksonTester<ImageDTO> JSONimageDTO;

    @InjectMocks
    ImageController imageController;

    @Mock
    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerAdvice())
                .build();

        imageDTO = new ImageDTO();
        imageDTO.setNumDocumento("1012443736");
        imageDTO.setImagen64("imagen64");
    }

    @Test
    void createImagen() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/imagen/create").contentType(MediaType.APPLICATION_JSON)
                                .content(JSONimageDTO.write(imageDTO).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void getImagenById() throws Exception{
        //given
        given(imageService.findById("1012443736"))
                .willReturn(Optional.of(imageDTO));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/imagen/getBy/1012443736")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(StandardCharsets.UTF_8), JSONimageDTO.write(imageDTO).getJson());
    }

    @Test
    void updateImagen() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        put("/imagen/update").contentType(MediaType.APPLICATION_JSON)
                                .content(JSONimageDTO.write(imageDTO).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void deleteImagen() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        delete("/imagen/delete/1012443736").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
        assertTrue(response.getContentAsString().isEmpty());
    }
}