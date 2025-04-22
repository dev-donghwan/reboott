package com.donghwan.team_reboott.company.infra

import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import com.donghwan.team_reboott.company.infra.jpa.CompanyJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class CompanyRepositoryImpl(
    private val jpaRepository: CompanyJpaRepository
) : CompanyRepository {

    override fun save(company: Company): Company {
        return jpaRepository.save(company)
    }

    override fun getById(companyId: Long): Company {
        return jpaRepository.findById(companyId).orElseThrow { throw GlobalException(ErrorCode.ENTITY_NOT_FOUND) }
    }

    override fun getAll(pageable: Pageable): Page<Company> {
        return jpaRepository.findAll(pageable)
    }
}