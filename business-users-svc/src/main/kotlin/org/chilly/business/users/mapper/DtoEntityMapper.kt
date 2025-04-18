package org.chilly.business.users.mapper

interface DtoEntityMapper<E : Any, D : Any> {
    fun toDto(entity: E): D
    fun toEntity(dto: D): E
}