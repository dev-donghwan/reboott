package com.donghwan.team_reboott.aifeature.domain.policy.impl

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.policy.FeatureUsageCalculator
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PerMonthFeatureUsageCalculator(
    private val aiFeatureUsageRepository: AiFeatureUsageRepository
): FeatureUsageCalculator {
    override fun calculate(company: Company, feature: AiFeature, input: String): Long {
        val usagePolicy = feature.usagePolicy

        validateMonthlyUsageLimit(company, feature)

        val cost = usagePolicy.costPerRequest.toLong()
        company.credit.canUse(cost)

        return cost
    }

    override fun apply(limitUnit: LimitUnit): Boolean {
        return limitUnit == LimitUnit.PER_MONTH
    }

    private fun validateMonthlyUsageLimit(company: Company, feature: AiFeature) {
        val now = LocalDateTime.now()
        val monthAgo = now.minusDays(30)

        val recentCount = aiFeatureUsageRepository.countByCompanyIdAndFeatureIdAndUsedAtBetween(
            companyId = company.id,
            featureId = feature.getIdOrThrow(),
            start = monthAgo,
            end = now,
        )

        require(recentCount < feature.usagePolicy.limitPerUnit) { throw GlobalException(ErrorCode.FREQUENCY_LIMIT_EXCEEDED)}
    }
}