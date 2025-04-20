package com.donghwan.team_reboott.aifeature.infra

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import org.springframework.data.jpa.repository.JpaRepository

interface AiFeatureJpaRepository : JpaRepository<AiFeature, Long>