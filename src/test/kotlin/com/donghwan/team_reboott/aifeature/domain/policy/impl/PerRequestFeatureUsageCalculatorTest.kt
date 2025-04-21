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

class PerRequestFeatureUsageCalculatorTest {

    private val calculator = PerRequestFeatureUsageCalculator()

    private fun feature(limit: Int = 10, cost: Int = 100): AiFeature {
        return AiFeature.create("Test", cost, limit, LimitUnit.PER_REQUEST)
    }

    private fun company(creditAmount: Long): Company {
        return Company.create("Reboott", CompanyCredit.create(creditAmount))
    }

    @Nested
    inner class Calculate {

        @Test
        fun success_calculate_with_valid_input() {
            val company = company(500)
            val feature = feature(limit = 10, cost = 100)

            val result = calculator.calculate(company, feature, input = "hello")

            assertThat(result).isEqualTo(100L)
        }

        @Test
        fun failure_when_input_length_exceeds_limit() {
            val company = company(500)
            val feature = feature(limit = 5, cost = 100)

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "toolonginput")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.INPUT_LIMIT_EXCEEDED)
        }

        @Test
        fun failure_when_credit_is_not_enough() {
            val company = company(10) // 부족한 크레딧
            val feature = feature(limit = 10, cost = 100)

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "valid")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_ENOUGH_CREDIT)
        }
    }

    @Nested
    inner class Apply {

        @Test
        fun success_apply_when_unit_matches() {
            assertThat(calculator.apply(LimitUnit.PER_REQUEST)).isTrue()
        }

        @Test
        fun success_apply_when_unit_not_matches() {
            assertThat(calculator.apply(LimitUnit.PER_MONTH)).isFalse()
        }
    }
}