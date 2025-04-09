package com.chilly.feedback_svc.mapper

interface DtoEntityMapper<E : Any, D : Any> {
    fun toDto(entity: E): D
    fun toEntity(dto: D): E
}