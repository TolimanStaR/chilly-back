package org.chilly.business.users.model

import jakarta.persistence.*
import org.chilly.business.users.mapper.BusinessCategoryToStringConverter
import org.chilly.business.users.mapper.ListToStringConverter

@Entity(name = "business_user")
@Table(name = "business_users")
class BusinessUser (
    @Id
    @Column(name = "id")
    var id: Long,

    @Column(name = "company_name", nullable = false)
    var companyName: String,

    @Column(name = "company_description", nullable = false)
    var companyDescription: String,

    @Column(name = "legal_address", nullable = false)
    var legalAddress: String,

    @Column(name = "inn", nullable = false)
    var inn: String,

    @Column(name = "okved", nullable = false)
    @Convert(converter = BusinessCategoryToStringConverter::class)
    var businessCategories: List<BusinessCategory>,

    @Column(name = "kpp", nullable = true)
    var kpp: String? = null,

    @Column(name = "images", nullable = true)
    @Convert(converter = ListToStringConverter::class)
    var images: List<String>? = null
) {

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    var requests: MutableSet<PlaceAddRequest> = mutableSetOf()
}