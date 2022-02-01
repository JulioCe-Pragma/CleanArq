package com.ruta.backend.cleanarq.person.infrastructure.adapters.out.repositories;

import com.ruta.backend.cleanarq.person.application.PersonMapperHelper;
import com.ruta.backend.cleanarq.person.application.ports.out.PersonRepository;
import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.domain.Person;
import com.ruta.backend.cleanarq.person.infrastructure.dbo.PersonEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonDboRepository implements PersonRepository {

    private final JpaPersonRepository jpaRepository;

    @Override
    public boolean existsById(String id){
        return jpaRepository.existsById(id);
    }

    @Override
    public void save(Person person){
        PersonEntity personEntity = PersonMapperHelper.modelMapper().map(person, PersonEntity.class);
        jpaRepository.save(personEntity);
    }

    @Override
    public Optional<Person> findById(String id){
        Optional<PersonEntity> personEntity = jpaRepository.findById(id);
        return personEntity.map(this::convertToPerson);
    }

    @Override
    public Optional<Person> findByTipoDocumentoAndNumDocumento(DocumentType documentType, String id){
        Optional<PersonEntity> personEntity = jpaRepository.findByTipoDocumentoAndNumDocumento(documentType,id);
        return personEntity.map(this::convertToPerson);
    }

    @Override
    public List<Person> findAllByEdadGreaterThanEqual(int age){
        List<PersonEntity> personEntity = jpaRepository.findAllByEdadGreaterThanEqual(age);
        return personEntity.stream().map(this::convertToPerson).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id){
        jpaRepository.deleteById(id);
    }

    private Person convertToPerson(final PersonEntity personEntity){
        return PersonMapperHelper.modelMapper().map(personEntity, Person.class);
    }
}
