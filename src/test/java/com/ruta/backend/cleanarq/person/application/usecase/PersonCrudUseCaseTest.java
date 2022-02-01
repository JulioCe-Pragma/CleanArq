package com.ruta.backend.cleanarq.person.application.usecase;

import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonAlreadyExist;
import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonNotFound;
import com.ruta.backend.cleanarq.image.application.ports.out.ImageRepository;
import com.ruta.backend.cleanarq.person.application.ports.out.PersonRepository;
import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.domain.Person;
import com.ruta.backend.cleanarq.person.infrastructure.adapters.out.repositories.JpaPersonRepository;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonCrudUseCaseTest {

    @InjectMocks
    PersonCrudUseCase personCrudUseCase;

    @Mock
    PersonRepository personRepository;

    @Mock
    ImageRepository imageRepository;

    Person person;
    PersonDTOut personDTOut;
    DocumentType documentType = DocumentType.CC;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        person = new Person();
        person.setNombres("pedro carlos");
        person.setApellidos("gomez torres");
        person.setNumDocumento("1012443736");
        person.setTipoDocumento(documentType);
        person.setEdad(24);
        person.setCiudad("Bogotá");
        person.setImagen(false);

        personDTOut = new PersonDTOut();
        personDTOut.setNombres("pedro carlos");
        personDTOut.setApellidos("gomez torres");
        personDTOut.setNumDocumento("1012443736");
        personDTOut.setTipoDocumento(documentType);
        personDTOut.setEdad(24);
        personDTOut.setCiudad("Bogotá");
        personDTOut.setImagen(false);
    }

    @Test
    void save() {
        when(personRepository.existsById("1012443736")).thenReturn(false);
        personCrudUseCase.save(person);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void saveError() {
        when(personRepository.existsById("1012443736")).thenReturn(true);
        assertThrows(PersonAlreadyExist.class, () -> { personCrudUseCase.save(person); });
    }

    @Test
    void findByDocumentTypeAndNumDocument() {
        Optional<Person> personOptional = Optional.of(person);
        when(personRepository.findByTipoDocumentoAndNumDocumento(documentType,"1012443736")).thenReturn(personOptional);
        assertEquals(Optional.of(personDTOut), personCrudUseCase.findByDocumentTypeAndNumDocument("CC","1012443736"));
    }

    @Test
    void findByDocumentTypeAndNumDocumentError() {
        when(personRepository.findByTipoDocumentoAndNumDocumento(documentType,"1012443736")).thenReturn(Optional.empty());
        assertThrows(PersonNotFound.class, () -> { personCrudUseCase.findByDocumentTypeAndNumDocument("CC", "1012443736"); });
    }

    @Test
    void findAllByAgeGreaterThanEqual() {
        List<PersonDTOut> personDTOList = new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        personDTOList.add(personDTOut);
        personList.add(person);
        when(personRepository.findAllByEdadGreaterThanEqual(18)).thenReturn(personList);
        assertEquals(personDTOList,personCrudUseCase.findAllByAgeGreaterThanEqual(18));
    }

    @Test
    void update() {
        when(personRepository.existsById("1012443736")).thenReturn(true);
        personCrudUseCase.update(person);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void updateError() {
        when(personRepository.existsById("1012443736")).thenReturn(false);
        assertThrows(PersonNotFound.class, () -> { personCrudUseCase.update(person); });
    }

    @Test
    void deleteById() {
        when(personRepository.findById("1012443736")).thenReturn(Optional.of(person));
        personCrudUseCase.deleteById("1012443736");
        verify(personRepository, times(1)).deleteById("1012443736");
    }

    @Test
    void deleteByIdImageTrue() {
        person.setImagen(true);
        when(personRepository.findById("1012443736")).thenReturn(Optional.of(person));
        personCrudUseCase.deleteById("1012443736");
        verify(imageRepository, times(1)).deleteById("1012443736");
    }

    @Test
    void deleteByIdError() {
        when(personRepository.existsById("1012443736")).thenReturn(false);
        assertThrows(PersonNotFound.class, () -> { personCrudUseCase.deleteById("1012443736"); });
    }
}