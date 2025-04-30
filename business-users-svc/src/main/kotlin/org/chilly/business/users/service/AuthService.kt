package org.chilly.business.users.service

import org.chilly.business.users.mapper.BusinessCategoryMapper
import org.chilly.business.users.model.BusinessCategory
import org.chilly.business.users.model.BusinessUser
import org.chilly.business.users.repository.UserRepository
import org.chilly.common.dto.BusinessUserDto
import org.chilly.common.dto.RegisterBusinessUserRequest
import org.chilly.common.dto.RegisterInternalRequest
import org.chilly.common.dto.UsernameData
import org.chilly.common.exception.CallFailedException
import org.chilly.common.exception.EntityExistsException
import org.chilly.common.exception.NoSuchEntityException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AuthService(
    private val repository: UserRepository,
    private val categoryMapper: BusinessCategoryMapper,
    private val webClient: WebClient,
) {

    fun register(request: RegisterBusinessUserRequest) {
        checkBusinessInfo(request)

        val userId = callSecurityRegister(request.toInternal())
            .getOrElse {
                println("error: ${it::class.simpleName}${it.message}")
                // todo if can be parsed then rethrow original
                throw CallFailedException("Internal register in security service failed")
            }

        repository.save(request.toEntity(userId))
    }

    fun getMe(userId: Long): BusinessUserDto {
        val usernameData = callSecurityMe(userId)
            .getOrElse {
                println("got exception: ${it::class.simpleName}, ${it.message}")
                throw CallFailedException("unable to get username data")
            }
        val businessUser = repository.findByIdOrNull(userId)
            ?: throw NoSuchEntityException("Not found user with id=$userId")

        return BusinessUserDto(
            /* email = */ usernameData.email,
            /* phoneNumber = */ usernameData.phoneNumber,
            /* companyName = */ businessUser.companyName,
            /* legalAddress = */ businessUser.legalAddress,
            /* inn = */ businessUser.inn,
            /* businessCategories = */ businessUser.businessCategories.map(categoryMapper::toDto),
            /* kpp = */ businessUser.kpp
        )
    }

    private fun callSecurityRegister(request: RegisterInternalRequest): Result<Long> =
        webClient.post()
            .uri("http://security-svc/api/auth/register/internal")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .awaitResult()

    private fun RegisterBusinessUserRequest.toInternal(): RegisterInternalRequest = RegisterInternalRequest(
        this.phoneNumber,
        this.email,
        this.password,
        listOf("BUSINESS")
    )

    private fun RegisterBusinessUserRequest.toEntity(userId: Long): BusinessUser = BusinessUser(
        id = userId,
        companyName = this.companyName,
        legalAddress = this.legalAddress,
        inn = this.inn,
        businessCategories = this.businessCategories.map { BusinessCategory(it.code, it.name) },
        kpp = this.kpp
    )

    private fun checkBusinessInfo(request: RegisterBusinessUserRequest) {
        val exception = when {
            repository.findByCompanyName(request.companyName) != null -> EntityExistsException("company name is already taken")
            repository.findByInn(request.inn) != null -> EntityExistsException("inn already taken")
            else -> null
        }
        exception?.let { throw it }
    }

    private fun callSecurityMe(id: Long): Result<UsernameData> =
        webClient.get()
            .uri("http://security-svc/api/auth/me/$id/internal")
            .awaitResult()

}