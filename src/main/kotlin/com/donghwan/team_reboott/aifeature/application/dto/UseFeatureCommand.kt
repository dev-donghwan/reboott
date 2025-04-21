package com.donghwan.team_reboott.aifeature.application.dto

data class UseFeatureCommand(
    val companyId: Long,
    val featureId: Long,
    val input: String
)
