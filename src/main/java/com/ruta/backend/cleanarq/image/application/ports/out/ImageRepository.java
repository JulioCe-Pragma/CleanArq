package com.ruta.backend.cleanarq.image.application.ports.out;

import com.ruta.backend.cleanarq.image.domain.Image;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository {

    void save(Image image);

    Optional<Image> findById(String id);

    void deleteById(String id);
}
