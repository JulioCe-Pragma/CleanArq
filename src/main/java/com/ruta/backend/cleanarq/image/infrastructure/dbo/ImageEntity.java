package com.ruta.backend.cleanarq.image.infrastructure.dbo;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;

@Data
@Generated
public class ImageEntity {

    @Id
    private String numDocumento;
    private String imagen64;
}
