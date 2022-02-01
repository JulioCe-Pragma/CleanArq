package com.ruta.backend.cleanarq.image.application.usecase;

import com.ruta.backend.cleanarq.image.domain.Image;
import com.ruta.backend.cleanarq.image.infrastructure.dto.ImageDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Validated
public interface ImageService {

    void save(@Valid Image image);

    Optional<ImageDTO> findById(String id);

    void update(@Valid Image image);

    void deleteById(String id);
}
