package com.donghwan.team_reboott.aifeature.domain.policy.impl

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.policy.FeatureUsageCalculator
import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.stereotype.Component

@Component
class PerMonthFeatureUsageCalculator: FeatureUsageCalculator {
    override fun calculate(company: Company, feature: AiFeature, input: String): Long {
        val usagePolicy = feature.usagePolicy

        // TODO add recently count 조회
        // repository.find(LocalDateTime.now.minus(1 month), companyId, aiFeatureId)

        val cost = usagePolicy.costPerRequest.toLong()
        company.credit.canUse(cost)

        return cost
    }

    override fun apply(limitUnit: LimitUnit): Boolean {
        return limitUnit == LimitUnit.PER_MONTH
    }
}