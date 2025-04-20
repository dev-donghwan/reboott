package com.donghwan.team_reboott.aifeature.domain.model

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
data class UsagePolicy(
    val costPerRequest: Int,
    val limitPerUnit: Int,
    @Enumerated(EnumType.STRING)
    val limitUnit: LimitUnit
)