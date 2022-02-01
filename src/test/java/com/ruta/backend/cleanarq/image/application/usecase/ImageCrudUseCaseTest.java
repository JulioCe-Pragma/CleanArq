package com.ruta.backend.cleanarq.image.application.usecase;

import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonNotFound;
import com.ruta.backend.cleanarq.image.application.ports.out.ImageRepository;
import com.ruta.backend.cleanarq.image.domain.Image;
import com.ruta.backend.cleanarq.image.infrastructure.dto.ImageDTO;
import com.ruta.backend.cleanarq.person.application.ports.out.PersonRepository;
import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageCrudUseCaseTest {

    @InjectMocks
    ImageCrudUseCase imageCrudUseCase;

    @Mock
    ImageRepository imageRepository;

    @Mock
    PersonRepository personRepository;

    Image image;
    ImageDTO imageDTO;
    Person person;
    DocumentType documentType = DocumentType.CC;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        image = new Image();
        image.setNumDocumento("1012443736");
        image.setImagen64("image64");

        imageDTO = new ImageDTO();
        imageDTO.setNumDocumento("1012443736");
        imageDTO.setImagen64("image64");

        person = new Person();
        person.setNombres("pedro carlos");
        person.setApellidos("gomez torres");
        person.setNumDocumento("1012443736");
        person.setTipoDocumento(documentType);
        person.setEdad(24);
        person.setCiudad("Bogotá");
        person.setImagen(false);

    }

    @Test
    void save() {
        Optional<Person> optionalPerson = Optional.of(person);
        when(personRepository.findById("1012443736")).thenReturn(optionalPerson);
        imageCrudUseCase.save(image);
        verify(imageRepository, times(1)).save(image);
        verify(personRepository, times(1)).save(optionalPerson.get());
    }

    @Test
    void saveError() {
        when(personRepository.findById("1012443736")).thenReturn(Optional.empty());
        assertThrows(PersonNotFound.class,() -> { imageCrudUseCase.save(image); });
    }

    @Test
    void findById() {
        when(personRepository.existsById("1012443736")).thenReturn(true);
        when(imageRepository.findById("1012443736")).thenReturn(Optional.of(image));
        assertEquals(Optional.of(imageDTO), imageCrudUseCase.findById("1012443736"));
    }

    @Test
    void findByIdError() {
        when(personRepository.existsById("1012443736")).thenReturn(false);
        assertThrows(PersonNotFound.class,() -> { imageCrudUseCase.findById("1012443736"); });
    }

    @Test
    void update() {
        when(personRepository.existsById("1012443736")).thenReturn(true);
        imageCrudUseCase.update(image);
        verify(imageRepository, times(1)).save(image);
    }

    @Test
    void updateError() {
        when(personRepository.existsById("1012443736")).thenReturn(false);
        assertThrows(PersonNotFound.class, () -> { imageCrudUseCase.update(image); });
    }

    @Test
    void deleteById() {
        Optional<Person> optionalPerson = Optional.of(person);
        when(personRepository.findById("1012443736")).thenReturn(optionalPerson);
        imageCrudUseCase.deleteById("1012443736");
        verify(imageRepository, times(1)).deleteById("1012443736");
        verify(personRepository, times(1)).save(optionalPerson.get());
    }

    @Test
    void deleteByIdError() {
        when(personRepository.findById("1012443736")).thenReturn(Optional.empty());
        assertThrows(PersonNotFound.class,() -> { imageCrudUseCase.deleteById("1012443736"); });
    }
}