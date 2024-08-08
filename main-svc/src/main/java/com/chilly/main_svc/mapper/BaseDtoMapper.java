package com.chilly.main_svc.mapper;

import org.modelmapper.ModelMapper;

public abstract class BaseDtoMapper<E, D> {

    private final ModelMapper mapper = new ModelMapper();

    protected abstract Class<E> getEntityClass();

    protected abstract Class<D> getDtoClass();

    public D toDto(E entity) {
        return mapper.map(entity, getDtoClass());
    }

    public E toModel(D dto) {
        return mapper.map(dto, getEntityClass());
    }
}
