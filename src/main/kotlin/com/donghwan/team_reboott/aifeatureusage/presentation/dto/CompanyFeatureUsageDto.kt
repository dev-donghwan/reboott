package com.donghwan.team_reboott.aifeatureusage.presentation.dto

import java.time.LocalDateTime

data class CompanyFeatureUsageDto(
    val companyId: Long,
    val companyName: String,
    val featureId: Long,
    val featureName: String,
    val usedCredit: Long,
    val inputLength: Int,
    val usedAt: LocalDateTime
)