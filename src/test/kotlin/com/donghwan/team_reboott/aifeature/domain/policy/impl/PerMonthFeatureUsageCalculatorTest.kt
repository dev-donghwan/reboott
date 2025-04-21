package com.donghwan.team_reboott.aifeature.domain.policy.impl

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PerMonthFeatureUsageCalculatorTest {

    private val calculator = PerMonthFeatureUsageCalculator()

    private fun feature(cost: Int = 100): AiFeature {
        return AiFeature.create("Monthly Feature", cost, 9999, LimitUnit.PER_MONTH)
    }

    private fun company(creditAmount: Long): Company {
        return Company.create("Reboott", CompanyCredit.create(creditAmount))
    }

    @Nested
    inner class Calculate {

        @Test
        fun success_calculate_when_credit_sufficient() {
            val company = company(500)
            val feature = feature(cost = 100)

            val result = calculator.calculate(company, feature, input = "any")
            assertThat(result).isEqualTo(100L)
        }

        @Test
        fun failure_calculate_when_credit_insufficient() {
            val company = company(50)
            val feature = feature(cost = 100)

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "whatever")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_ENOUGH_CREDIT)
        }
    }

    @Nested
    inner class Apply {

        @Test
        fun success_apply_when_unit_matches() {
            assertThat(calculator.apply(LimitUnit.PER_MONTH)).isTrue()
        }

        @Test
        fun success_apply_when_unit_does_not_match() {
            assertThat(calculator.apply(LimitUnit.PER_REQUEST)).isFalse()
        }
    }
}