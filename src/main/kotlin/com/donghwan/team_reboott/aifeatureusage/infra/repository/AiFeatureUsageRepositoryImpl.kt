package com.donghwan.team_reboott.aifeatureusage.infra.repository

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AiFeatureUsageRepositoryImpl(
    private val aiFeatureUsageJpaRepository: AiFeatureUsageJpaRepository
) : AiFeatureUsageRepository {
    override fun save(usage: AiFeatureUsage): AiFeatureUsage {
        return aiFeatureUsageJpaRepository.save(usage)
    }

    override fun findByCompanyIdAndUsedAtBetween(companyId: Long, start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<AiFeatureUsageProjectionDto> {
        return aiFeatureUsageJpaRepository.findUsageByCompanyIdAndDateRange(companyId, start, end, pageable)
    }

    override fun findByCompanyIdAndFeatureIdAndUsedAtBetween(companyId: Long, featureId: Long, start: LocalDateTime, end: LocalDateTime, pageable: Pageable): Page<AiFeatureUsageProjectionDto> {
        return aiFeatureUsageJpaRepository.findUsageByCompanyIdAndFeatureIdAndDateRange(companyId, featureId, start, end, pageable)
    }

    override fun countByCompanyIdAndFeatureIdAndUsedAtBetween(companyId: Long, featureId: Long, start: LocalDateTime, end: LocalDateTime): Long {
        return aiFeatureUsageJpaRepository.countByCompanyIdAndFeatureIdAndUsedAtBetween(companyId, featureId, start, end)
    }
}