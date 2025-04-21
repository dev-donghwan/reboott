package com.donghwan.team_reboott.company.infra.jpa

import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyCreditJpaRepository: JpaRepository<CompanyCredit, Long> {
}