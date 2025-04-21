package com.donghwan.team_reboott.aifeature.domain.policy

import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.springframework.stereotype.Component

@Component
class FeatureUsageCalculatorRouter(
    private val calculators: List<FeatureUsageCalculator>
) {
    fun get(unit: LimitUnit): FeatureUsageCalculator {
        return calculators.find { it.apply(unit) } ?: throw GlobalException(ErrorCode.NOT_IMPLEMENTED)
    }
}