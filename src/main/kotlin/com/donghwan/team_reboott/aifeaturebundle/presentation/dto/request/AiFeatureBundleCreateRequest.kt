package com.donghwan.team_reboott.aifeaturebundle.presentation.dto.request

data class AiFeatureBundleCreateRequest(
    val name: String,
    val featureIds: Set<Long>
)
