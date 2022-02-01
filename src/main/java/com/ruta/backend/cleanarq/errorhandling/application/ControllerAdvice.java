package com.ruta.backend.cleanarq.errorhandling.application;

import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonAlreadyExist;
import com.ruta.backend.cleanarq.errorhandling.domain.exceptions.PersonNotFound;
import com.ruta.backend.cleanarq.errorhandling.domain.responses.ResponseError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {

    static final String INVALID_ARG = "Argumento invalido";

    @ExceptionHandler(value = {PersonAlreadyExist.class})
    protected ResponseEntity<Object> handleAlreadyExist() {
        return new ResponseEntity<>(new ResponseError("Persona ya registrada","Se está intentando crear una persona que ya existe"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PersonNotFound.class})
    protected ResponseEntity<Object> handleNotExist() {
        return new ResponseEntity<>(new ResponseError("Persona no encontrada","La persona sobre la cual se intentó la operación, no existe"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraint(ConstraintViolationException e) {
        return new ResponseEntity<>(new ResponseError(INVALID_ARG, e.getMessage().replaceAll(
                "(get)*(delete)*[a-zA-Z\\.]+(:\\p{javaSpaceChar}){1}","")),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegal() {
        return new ResponseEntity<>(new ResponseError(INVALID_ARG, "Se ha introducido un valor no aceptado por el campo" +
                " tipoDocumento. Los valores aceptados son: [CC, CT, CE]"),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getMessage();
        if(message != null){
            if(message.contains("tipoDocumento")){
                return new ResponseEntity<>(new ResponseError(INVALID_ARG, "Se ha introducido un valor no aceptado por el campo" +
                        " tipoDocumento. Los valores aceptados son: [CC, CT, CE]"),
                        HttpStatus.BAD_REQUEST);
            }
            if(message.contains("edad")){
                return new ResponseEntity<>(new ResponseError(INVALID_ARG, "La edad debe ser numerica"),
                        HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(new ResponseError("ERROR", message),
                        HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity<>(new ResponseError("ERROR FATAL", "comuniquese con el administrador"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseError(INVALID_ARG, errorList.toString()),
                HttpStatus.BAD_REQUEST);
    }
}
