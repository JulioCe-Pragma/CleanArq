package com.ruta.backend.cleanarq.person.application;

import org.modelmapper.ModelMapper;

public class PersonMapperHelper {

    private PersonMapperHelper(){}
    public static ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
