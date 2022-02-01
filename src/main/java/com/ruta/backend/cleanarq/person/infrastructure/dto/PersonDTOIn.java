package com.ruta.backend.cleanarq.person.infrastructure.dto;

import com.ruta.backend.cleanarq.person.domain.DocumentType;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class PersonDTOIn {
    private String nombres;
    private String apellidos;
    private DocumentType tipoDocumento;
    private String numDocumento;
    private int edad;
    private String ciudad;
}
