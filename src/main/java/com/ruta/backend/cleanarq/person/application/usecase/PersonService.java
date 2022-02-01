package com.ruta.backend.cleanarq.person.application.usecase;

import com.ruta.backend.cleanarq.person.domain.Person;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOut;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public interface PersonService {

    void save(@Valid Person person);

    Optional<PersonDTOut> findByDocumentTypeAndNumDocument(String documentType, String personId);

    List<PersonDTOut> findAllByAgeGreaterThanEqual(int age);

    void update(@Valid Person person);

    void deleteById(String personId);
}
