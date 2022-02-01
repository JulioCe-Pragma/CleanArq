package com.ruta.backend.cleanarq.person.application.ports.out;

import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.domain.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository {

    boolean existsById(String id);

    void save(Person person);

    Optional<Person> findById(String id);

    Optional<Person> findByTipoDocumentoAndNumDocumento(DocumentType documentType, String id);

    List<Person> findAllByEdadGreaterThanEqual(int age);

    void deleteById(String id);
}
