package com.ruta.backend.cleanarq.image.infrastructure.adapters.out.repositories;

import com.ruta.backend.cleanarq.image.application.ImageMapperHelper;
import com.ruta.backend.cleanarq.image.application.ports.out.ImageRepository;
import com.ruta.backend.cleanarq.image.domain.Image;
import com.ruta.backend.cleanarq.image.infrastructure.dbo.ImageEntity;
import com.ruta.backend.cleanarq.person.application.PersonMapperHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageDboRepository implements ImageRepository {

    private final MongoImageRepository mongoImageRepository;

    @Override
    public void save(Image image) {
        ImageEntity imageEntity = ImageMapperHelper.modelMapper().map(image, ImageEntity.class);
        mongoImageRepository.save(imageEntity);
    }

    @Override
    public Optional<Image> findById(String id) {
        Optional<ImageEntity> imageEntity = mongoImageRepository.findById(id);
        return imageEntity.map(this::convertToImage);
    }

    @Override
    public void deleteById(String id) {
        mongoImageRepository.deleteById(id);
    }

    private Image convertToImage(final ImageEntity imageEntity){
        return PersonMapperHelper.modelMapper().map(imageEntity, Image.class);
    }
}
