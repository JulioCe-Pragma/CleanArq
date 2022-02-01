package com.ruta.backend.cleanarq.person.infrastructure.adapters.out.repositories;

import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.infrastructure.dbo.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPersonRepository extends CrudRepository <PersonEntity, String> {

    @Transactional(readOnly = true)
    Optional<PersonEntity> findByTipoDocumentoAndNumDocumento(DocumentType documentType, String personId);

    @Transactional(readOnly = true)
    List<PersonEntity> findAllByEdadGreaterThanEqual(int age);
}
