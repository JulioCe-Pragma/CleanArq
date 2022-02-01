package com.ruta.backend.cleanarq.person.infrastructure.dbo;

import com.ruta.backend.cleanarq.person.domain.DocumentType;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Generated
@Table(name = "persons")
public class PersonEntity {

    private String nombres;
    private String apellidos;
    @Enumerated(value = EnumType.STRING)
    private DocumentType tipoDocumento;
    @Id
    @Size(max = 10)
    private String numDocumento;
    private int edad;
    private String ciudad;
    private boolean imagen;
}
