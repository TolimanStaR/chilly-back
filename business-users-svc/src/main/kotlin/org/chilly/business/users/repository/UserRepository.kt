package org.chilly.business.users.repository

import org.chilly.business.users.model.BusinessUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<BusinessUser, Long> {

    fun findByCompanyName(companyName: String): BusinessUser?

    fun findByInn(inn: String): BusinessUser?

}