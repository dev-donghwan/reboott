package com.donghwan.team_reboott.aifeatureusage.domain.repository

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface AiFeatureUsageRepository {

    fun save(usage: AiFeatureUsage): AiFeatureUsage

    fun findByCompanyIdAndUsedAtBetween(
        companyId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto>

    fun findByCompanyIdAndFeatureIdAndUsedAtBetween(
        companyId: Long,
        featureId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto>

    fun countByCompanyIdAndFeatureIdAndUsedAtBetween(
        companyId: Long,
        featureId: Long,
        start: LocalDateTime,
        end: LocalDateTime
    ): Long
}