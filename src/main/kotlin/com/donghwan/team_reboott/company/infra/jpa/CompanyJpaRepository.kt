package com.donghwan.team_reboott.company.infra.jpa

import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CompanyJpaRepository : JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c JOIN FETCH c._bundle WHERE c._id = :id")
    override fun findById(id: Long): Optional<Company>
}