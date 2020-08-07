package com.gurgaczj.jmaker.service;

import org.modelmapper.ModelMapper;

public interface DtoMapper {

    static <T> T toDto(Object source, Class<T> destination){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destination);
    }
}
