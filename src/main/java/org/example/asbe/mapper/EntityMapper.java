package org.example.asbe.mapper;


import java.util.List;

public interface EntityMapper <D, E> {
    E toEntity(D dto);
    D toDto(E entity);

    List<E> toEntities(List<D> dtoList);
    List <D> toDtoList(List<E> entityList);

}