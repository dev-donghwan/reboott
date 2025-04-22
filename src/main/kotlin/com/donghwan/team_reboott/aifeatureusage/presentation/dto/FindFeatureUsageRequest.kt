package com.donghwan.team_reboott.aifeatureusage.presentation.dto

import java.time.LocalDate

data class FindFeatureUsageRequest(
    val companyId: Long,
    val featureId: Long? = null,
    val startDate: LocalDate,
    val endDate: LocalDate
)