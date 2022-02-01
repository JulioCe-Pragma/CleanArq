package com.ruta.backend.cleanarq.errorhandling.domain.exceptions;

public class PersonNotFound extends RuntimeException{

    public PersonNotFound() {
        // Este metodo va vacio ya que solo sirve para disparar un cierto tipo de error que será procesado por controllerAdvice
    }
}
