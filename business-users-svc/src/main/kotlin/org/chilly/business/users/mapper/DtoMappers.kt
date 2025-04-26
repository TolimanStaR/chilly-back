package org.chilly.business.users.mapper

import org.chilly.business.users.model.BusinessCategory
import org.chilly.common.dto.BusinessCategoryDto
import org.springframework.stereotype.Component

@Component
class BusinessCategoryMapper : DtoEntityMapper<BusinessCategory, BusinessCategoryDto> {
    override fun toDto(entity: BusinessCategory) = BusinessCategoryDto(
        entity.code,
        entity.name
    )

    override fun toEntity(dto: BusinessCategoryDto) = BusinessCategory(
        code = dto.code,
        name = dto.name
    )
}