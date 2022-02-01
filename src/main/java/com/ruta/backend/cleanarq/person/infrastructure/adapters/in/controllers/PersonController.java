package com.ruta.backend.cleanarq.person.infrastructure.adapters.in.controllers;

import com.ruta.backend.cleanarq.errorhandling.domain.responses.ResponseError;
import com.ruta.backend.cleanarq.person.application.PersonMapperHelper;
import com.ruta.backend.cleanarq.person.domain.Person;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOIn;
import com.ruta.backend.cleanarq.person.application.usecase.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonController {

    private PersonService personService;

    @Operation(summary = "Endpoint que permite crear o registrar una persona en base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Persona creada o registrada existosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Persona ya creada o registrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)) })
    })
    @PostMapping(value = "create/persona", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPersona(@RequestBody PersonDTOIn request) {
        this.personService.save(PersonMapperHelper.modelMapper().map(request, Person.class));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Endpoint que permite consultar/obtener una persona a traves de su tipo y numero de documento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona obtenida de forma exitosa",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonDTOIn.class))}),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)) })
    })
    @GetMapping(value = "getBy/TipoAndId/{tipo}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPersonaByTipoAndId(@PathVariable("tipo") String tipo, @PathVariable("id")
                                                            @Pattern(regexp = "^[0-9]+$", message = "El parametro id debe ser numerico")
                                                                @Size(min = 8, max = 10, message = "El parametro id debe tener entre 8 y 10 digitos")
                                                                    String id){
        return ResponseEntity.ok(this.personService.findByDocumentTypeAndNumDocument(tipo, id));
    }

    @Operation(summary = "Endpoint que permite obtener todas las personas con una edad mayor o igual a la consultada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada de forma exitosa",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTOIn.class)))}),
            @ApiResponse(responseCode = "404", description = "Consulta no realizada, edad menor a 18",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)) })
    })
    @GetMapping(value = "getByEdad/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPersonaByEdad(@PathVariable("edad")
                                                       @Min(value = 18, message = "El campo edad debe ser mayor o igual a 18")
                                                       @Max(value = 123, message = "El record de la persona mas logeva es de 123 años, rompiste ese record?")
                                                               int edad){
        return ResponseEntity.ok(this.personService.findAllByAgeGreaterThanEqual(edad));
    }

    @Operation(summary = "Endpoint que permite actualizar los datos de una persona ya registrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona actualizada existosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Actualizacion no exitosa, persona no encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)) })
    })
    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePersona(@RequestBody PersonDTOIn request){
        this.personService.update(PersonMapperHelper.modelMapper().map(request, Person.class));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Endpoint que permite eliminar una persona ya registrada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Persona eliminada existosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Eliminacion no exitosa, persona no encontrada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseError.class)) })
    })
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Object> deletePersona(@PathVariable("id") @Pattern(regexp = "^[0-9]+$", message = "El parametro id debe ser numerico")
                                                    @Size(min = 8, max = 10, message = "El parametro id debe tener entre 8 y 10 digitos")
                                                            String id){
        this.personService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
