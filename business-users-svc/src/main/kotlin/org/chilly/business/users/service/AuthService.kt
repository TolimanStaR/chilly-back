package org.chilly.business.users.service

import org.chilly.business.users.model.BusinessCategory
import org.chilly.business.users.model.BusinessUser
import org.chilly.business.users.repository.UserRepository
import org.chilly.common.dto.RegisterBusinessUserRequest
import org.chilly.common.dto.RegisterInternalRequest
import org.chilly.common.exception.CallFailedException
import org.chilly.common.exception.EntityExistsException
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class AuthService(
    private val repository: UserRepository,
    private val webClient: WebClient,
) {

    fun register(request: RegisterBusinessUserRequest) {
        checkBusinessInfo(request)

        val userId = callSecurity(request.toInternal())
            .getOrElse {
                println("error: ${it::class.simpleName}${it.message}")
                // todo if can be parsed then rethrow original
                throw CallFailedException("Internal register in security service failed")
            }

        repository.save(request.toEntity(userId))
    }

    private fun callSecurity(request: RegisterInternalRequest): Result<Long> = runCatching {
        webClient.post()
            .uri("http://security-svc/api/auth/register/internal")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Long::class.java)
            .block()!!
    }

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

}