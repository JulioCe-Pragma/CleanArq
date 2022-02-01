package com.ruta.backend.cleanarq.image.application.usecase;

import com.ruta.backend.cleanarq.image.application.ImageMapperHelper;
import com.ruta.backend.cleanarq.image.application.ports.out.ImageRepository;
import com.ruta.backend.cleanarq.image.domain.Image;
import com.ruta.backend.cleanarq.image.infrastructure.dto.ImageDTO;
import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonNotFound;
import com.ruta.backend.cleanarq.person.application.ports.out.PersonRepository;
import com.ruta.backend.cleanarq.person.domain.Person;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ImageCrudUseCase implements ImageService{

    private ImageRepository imageRepository;
    private PersonRepository personRepository;

    @Override
    public void save(Image image){
        Optional<Person> person = personRepository.findById(image.getNumDocumento());
        if(person.isPresent()){
            this.imageRepository.save(image);
            person.get().setImagen(true);
            this.personRepository.save(person.get());
        }else {
            throw new PersonNotFound();
        }
    }

    @Override
    public Optional<ImageDTO> findById(String id) {
        if(personRepository.existsById(id)){
            return this.imageRepository.findById(id).map(this::convertToImageDTO);
        }else {
            throw new PersonNotFound();
        }
    }

    @Override
    public void update(Image image){
        if(personRepository.existsById(image.getNumDocumento())){
            this.imageRepository.save(image);
        }else {
            throw new PersonNotFound();
        }
    }

    @Override
    public void deleteById(String personaId) {
        Optional<Person> person = personRepository.findById(personaId);
        if(person.isPresent()){
            this.imageRepository.deleteById(personaId);
            person.get().setImagen(false);
            this.personRepository.save(person.get());
        }else {
            throw new PersonNotFound();
        }
    }

    private ImageDTO convertToImageDTO(final Image image){
        return ImageMapperHelper.modelMapper().map(image, ImageDTO.class);
    }
}
