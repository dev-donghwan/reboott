package com.donghwan.team_reboott.aifeatureusage.infra.repository

import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import org.springframework.data.jpa.repository.JpaRepository

interface AiFeatureUsageJpaRepository : JpaRepository<AiFeatureUsage, Long>, AiFeatureUsageCustomRepository {
}