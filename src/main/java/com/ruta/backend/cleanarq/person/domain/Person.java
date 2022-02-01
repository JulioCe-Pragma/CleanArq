package com.ruta.backend.cleanarq.person.domain;

import lombok.Data;
import lombok.Generated;

import javax.validation.constraints.*;

@Data
@Generated
public class Person {

    @NotBlank(message = "El campo nombres no debe estár vacio")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "El campo nombres solo puede contener letras")
    private String nombres;

    @NotBlank (message = "El campo apellidos no debe estár vacio")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "El campo apellidos solo puede contener letras")
    private String apellidos;

    @NotNull(message = "El campo tipoDocumento no debe estár vacio")
    private DocumentType tipoDocumento;

    @NotEmpty(message = "el campo numDocumento no debe estár vacio")
    @Pattern(regexp = "^[0-9]+$", message = "El campo numDocumento debe ser numerico")
    @Size(min = 8, max = 10, message = "El campo numDocumento debe tener entre 8 y 10 digitos")
    private String numDocumento;

    @Min(value = 18, message = "El campo edad no debe estár vacio y debe ser mayor o igual a 18")
    @Max(value = 123, message = "El record de la persona mas logeva es de 123 años, rompiste ese record?")
    private int edad;

    @NotBlank (message = "El campo ciudad no debe estár vacio")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "El campo ciudad solo puede contener letras")
    private String ciudad;

    private boolean imagen;
}
