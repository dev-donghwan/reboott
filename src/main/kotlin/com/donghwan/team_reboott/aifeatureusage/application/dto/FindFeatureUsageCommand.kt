package com.donghwan.team_reboott.aifeatureusage.application.dto

import java.time.LocalDateTime

data class FindFeatureUsageCommand(
    val companyId: Long,
    val featureId: Long? = null,
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    fun hasFeatureId(): Boolean = featureId != null
}