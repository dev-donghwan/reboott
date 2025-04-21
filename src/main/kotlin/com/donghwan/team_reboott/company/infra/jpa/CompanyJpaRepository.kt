package com.donghwan.team_reboott.company.infra.jpa

import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyJpaRepository : JpaRepository<Company, Long> {

    @EntityGraph(attributePaths = ["bundle", "bundle.mappings", "bundle.mappings.feature"])
    fun findWithBundleAndFeaturesById(id: Long): Company?
}