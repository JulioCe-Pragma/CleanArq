package com.ruta.backend.cleanarq.image.infrastructure.adapters.out.repositories;

import com.ruta.backend.cleanarq.image.infrastructure.dbo.ImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoImageRepository extends MongoRepository <ImageEntity, String> {

}
