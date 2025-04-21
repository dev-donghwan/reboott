package com.donghwan.team_reboott.company.domain.repository

import com.donghwan.team_reboott.company.domain.model.Company

interface CompanyRepository {
    fun save(company: Company): Company
    fun getById(companyId: Long): Company
}