package com.ruta.backend.cleanarq.image.application;

import org.modelmapper.ModelMapper;

public class ImageMapperHelper {

    private ImageMapperHelper(){}

    public static ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
