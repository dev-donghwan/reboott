package com.donghwan.team_reboott.aifeature.domain.policy

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.company.domain.model.Company

interface FeatureUsageCalculator {
    fun calculate(company: Company, feature: AiFeature, input: String): Long
    fun apply(limitUnit: LimitUnit): Boolean
}