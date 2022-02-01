package com.ruta.backend.cleanarq.person.infrastructure.adapters.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruta.backend.cleanarq.errorhandling.application.ControllerAdvice;
import com.ruta.backend.cleanarq.person.application.usecase.PersonService;
import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOIn;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOut;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class PersonControllerTest {

    private MockMvc mvc;
    private PersonDTOut personDTOut;
    private PersonDTOIn personDTOIn;
    private DocumentType documentType = DocumentType.CC;
    private JacksonTester<PersonDTOIn> JSONpersonaDTOIn;
    private JacksonTester<PersonDTOut> JSONpersonaDTOut;

    @InjectMocks
    PersonController personController;

    @Mock
    PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(personController)
                .setControllerAdvice(new ControllerAdvice())
                .build();

        personDTOut = new PersonDTOut();
        personDTOut.setNombres("pedro carlos");
        personDTOut.setApellidos("gomez torres");
        personDTOut.setNumDocumento("1012443736");
        personDTOut.setTipoDocumento(documentType);
        personDTOut.setEdad(24);
        personDTOut.setCiudad("Bogotá");
        personDTOut.setImagen(false);

        personDTOIn = new PersonDTOIn();
        personDTOIn.setNombres("pedro carlos");
        personDTOIn.setApellidos("gomez torres");
        personDTOIn.setNumDocumento("1012443736");
        personDTOIn.setTipoDocumento(documentType);
        personDTOIn.setEdad(24);
        personDTOIn.setCiudad("Bogotá");
    }

    @Test
    void createPersona() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/create/persona").contentType(MediaType.APPLICATION_JSON)
                                .content(JSONpersonaDTOIn.write(personDTOIn).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void getPersonaByTipoAndId() throws Exception{
        //given
        given(personService.findByDocumentTypeAndNumDocument("CC","1012443736"))
                .willReturn(Optional.of(personDTOut));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/getBy/TipoAndId/CC/1012443736")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(StandardCharsets.UTF_8), JSONpersonaDTOut.write(personDTOut).getJson());
    }

    @Test
    void getPersonaByEdad() throws Exception{
        //given
        List<PersonDTOut> personDTOList = new ArrayList<>();
        personDTOList.add(personDTOut);

        given(personService.findAllByAgeGreaterThanEqual(18))
                .willReturn(personDTOList);

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/getByEdad/18")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        List<String> jsonList = new ArrayList<>();
        jsonList.add(JSONpersonaDTOut.write(personDTOut).getJson());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentAsString(StandardCharsets.UTF_8), jsonList.toString());
    }

    @Test
    void updatePersona() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        put("/update").contentType(MediaType.APPLICATION_JSON)
                                .content(JSONpersonaDTOut.write(personDTOut).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    void deletePersona() throws Exception{
        // when
        MockHttpServletResponse response = mvc.perform(
                        delete("/delete/1012443736").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
        assertTrue(response.getContentAsString().isEmpty());
    }
}