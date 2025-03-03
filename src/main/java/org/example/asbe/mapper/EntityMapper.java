package org.example.asbe.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);

    default List<E> toEntities(List<D> dtoList) {
        return dtoList == null ? List.of() : dtoList.stream().map(this::toEntity).toList();
    }

    default List<D> toDtoList(List<E> entityList) {
        return entityList == null ? List.of() : entityList.stream().map(this::toDto).toList();
    }

    default List<D> toResponseList(List<E> entityList) {
        return toDtoList(entityList);
    }
}
