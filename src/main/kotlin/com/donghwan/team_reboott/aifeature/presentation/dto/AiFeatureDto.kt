package com.donghwan.team_reboott.aifeature.presentation.dto

data class AiFeatureDto(
    val id: Long,
    val name: String,
    val costPerRequest: Int,
    val limitPerUnit: Int,
    val limitUnit: String
)
