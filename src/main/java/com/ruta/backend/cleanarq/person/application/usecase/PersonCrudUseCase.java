package com.ruta.backend.cleanarq.person.application.usecase;

import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonAlreadyExist;
import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonNotFound;
import com.ruta.backend.cleanarq.image.application.ports.out.ImageRepository;
import com.ruta.backend.cleanarq.person.application.PersonMapperHelper;
import com.ruta.backend.cleanarq.person.application.ports.out.PersonRepository;
import com.ruta.backend.cleanarq.person.domain.DocumentType;
import com.ruta.backend.cleanarq.person.domain.Person;
import com.ruta.backend.cleanarq.person.infrastructure.dto.PersonDTOut;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonCrudUseCase implements PersonService {

    private PersonRepository personRepository;
    private ImageRepository imageRepository;

    @Override
    public void save(Person person) {
        if(!this.personRepository.existsById(person.getNumDocumento())){
            this.personRepository.save(person);
        }else{
            throw new PersonAlreadyExist();
        }
    }

    @Override
    public Optional<PersonDTOut> findByDocumentTypeAndNumDocument(String documentType, String personId){
        DocumentType documentTypeEnum = DocumentType.valueOf(documentType);
        Optional<Person> person = this.personRepository.findByTipoDocumentoAndNumDocumento(documentTypeEnum, personId);
        if(person.isPresent()){
            return person.map(this::convertToPersonDTOut);
        }else{
            throw new PersonNotFound();
        }
    }

    @Override
    public List<PersonDTOut> findAllByAgeGreaterThanEqual(int age) {
        List<Person> person = this.personRepository.findAllByEdadGreaterThanEqual(age);
        return person.stream().map(this::convertToPersonDTOut).collect(Collectors.toList());
    }

    @Override
    public void update(Person person) {
        if(this.personRepository.existsById(person.getNumDocumento())){
            this.personRepository.save(person);
        }else{
            throw new PersonNotFound();
        }
    }

    @Override
    public void deleteById(String personId) {
        Optional<Person> person = this.personRepository.findById(personId);
        if(person.isPresent()){
            if(person.get().isImagen()){
                imageRepository.deleteById(personId);
            }
            this.personRepository.deleteById(personId);
        }else{
            throw new PersonNotFound();
        }
    }

    private PersonDTOut convertToPersonDTOut(final Person person){
        return PersonMapperHelper.modelMapper().map(person, PersonDTOut.class);
    }
}
