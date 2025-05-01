package org.chilly.business.users.mapper

import org.chilly.business.users.model.BusinessCategory
import org.chilly.business.users.model.BusinessUser
import org.chilly.business.users.model.PlaceAddRequest
import org.chilly.common.dto.BusinessCategoryDto
import org.chilly.common.dto.BusinessUserDto
import org.chilly.common.dto.PlaceDto
import org.chilly.common.dto.PlaceRequestDto
import org.chilly.common.dto.UsernameData
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

@Component
class BusinessUserMapper(private val categoryMapper: BusinessCategoryMapper) {
    fun toDto(user: BusinessUser, usernameData: UsernameData? = null) = BusinessUserDto(
        /* email = */ usernameData?.email,
        /* phoneNumber = */ usernameData?.phoneNumber,
        /* companyName = */ user.companyName,
        /* companyDescription = */ user.companyDescription,
        /* legalAddress = */ user.legalAddress,
        /* inn = */ user.inn,
        /* businessCategories = */ user.businessCategories.map(categoryMapper::toDto),
        /* kpp = */ user.kpp,
        /* images = */ user.images
    )
}

internal fun PlaceAddRequest.toDto() = PlaceRequestDto(
    /* id = */ id,
    /* ownerId = */ owner.id,
    /* timestamp = */ timestamp.toEpochMilli(),
    /* status = */ status.name,
    /* reason = */ reason,
    /* placeInfo = */ PlaceDto(
        /* id = */ null,
        /* name = */ name,
        /* address = */ address,
        /* website = */ website,
        /* yPage = */ yPage,
        /* rating = */ rating,
        /* images = */ images,
        /* phone = */ phone,
        /* social = */ social,
        /* openHours = */ openHours,
        /* latitude = */ latitude,
        /* longitude = */ longitude,
        /* ownerId = */ owner.id
    )
)

