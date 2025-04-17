package org.chilly.business.users.model

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.chilly.business.users.mapper.BusinessCategoryToStringConverter

@Entity(name = "business_user")
@Table(name = "business_users")
class BusinessUser (
    @Id
    @Column(name = "id")
    var id: Long,

    @Column(name = "company_name", nullable = false)
    var companyName: String,

    @Column(name = "legal_address", nullable = false)
    var legalAddress: String,

    @Column(name = "inn", nullable = false)
    var inn: String,

    @Column(name = "okved", nullable = false)
    @Convert(converter = BusinessCategoryToStringConverter::class)
    var businessCategories: List<BusinessCategory>,

    @Column(name = "kpp", nullable = true)
    var kpp: String? = null
)