package com.donghwan.team_reboott.aifeatureusage.infra.repository

import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface AiFeatureUsageJpaRepository : JpaRepository<AiFeatureUsage, Long>, AiFeatureUsageCustomRepository {

    fun countByCompanyIdAndFeatureIdAndUsedAtBetween(
        companyId: Long,
        featureId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ): Long
}