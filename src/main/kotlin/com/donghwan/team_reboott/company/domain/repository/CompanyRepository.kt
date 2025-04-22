package com.donghwan.team_reboott.company.domain.repository

import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CompanyRepository {
    fun save(company: Company): Company
    fun getById(companyId: Long): Company
    fun getAll(pageable: Pageable): Page<Company>
}