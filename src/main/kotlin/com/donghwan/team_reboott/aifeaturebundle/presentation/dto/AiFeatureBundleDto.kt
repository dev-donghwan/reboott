package com.donghwan.team_reboott.aifeaturebundle.presentation.dto

import com.donghwan.team_reboott.aifeature.presentation.dto.AiFeatureDto

data class AiFeatureBundleDto(
    val id: Long,
    val name: String,
    val features: List<AiFeatureDto>
)
