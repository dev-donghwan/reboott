package com.donghwan.team_reboott.aifeatureusage.infra.repository

import AiFeatureUsageProjectionDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface AiFeatureUsageCustomRepository {
    fun findUsageByCompanyIdAndDateRange(
        companyId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto>

    fun findUsageByCompanyIdAndFeatureIdAndDateRange(
        companyId: Long,
        featureId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto>
}