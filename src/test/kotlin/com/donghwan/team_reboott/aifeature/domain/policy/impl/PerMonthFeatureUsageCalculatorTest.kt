package com.donghwan.team_reboott.aifeature.domain.policy.impl

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.model.UsagePolicy
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class PerMonthFeatureUsageCalculatorTest {

    private val usageRepository: AiFeatureUsageRepository = mock()
    private val calculator = PerMonthFeatureUsageCalculator(usageRepository)

    private fun feature(cost: Int = 100, limit: Int = 9999, id: Long = 1L): AiFeature {
        return mock {
            on { getIdOrThrow() } doReturn id
            on { usagePolicy } doReturn UsagePolicy(cost, limit, LimitUnit.PER_MONTH)
        }
    }

    private fun company(creditAmount: Long, id: Long = 1L): Company {
        return mock {
            on { this.id } doReturn id
            on { credit } doReturn CompanyCredit.create(creditAmount)
        }
    }

    @Nested
    inner class Calculate {

        @Test
        fun success_calculate_when_credit_sufficient_and_usage_under_limit() {
            val company = company(500)
            val feature = feature(cost = 100, limit = 200)
            val companyId = company.id
            val featureId = feature.getIdOrThrow()

            whenever(
                usageRepository.countByCompanyIdAndFeatureIdAndUsedAtBetween(
                    eq(companyId),
                    eq(featureId),
                    any(),
                    any()
                )
            ).thenReturn(100L)

            val result = calculator.calculate(company, feature, input = "any")
            assertThat(result).isEqualTo(100L)
        }

        @Test
        fun failure_calculate_when_credit_insufficient() {
            val company = company(50)
            val feature = feature(cost = 100)
            val companyId = company.id
            val featureId = feature.getIdOrThrow()

            whenever(
                usageRepository.countByCompanyIdAndFeatureIdAndUsedAtBetween(
                    eq(companyId),
                    eq(featureId),
                    any(),
                    any()
                )
            ).thenReturn(0L)

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "low-credit")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_ENOUGH_CREDIT)
        }

        @Test
        fun failure_calculate_when_monthly_limit_exceeded() {
            val company = company(500)
            val feature = feature(cost = 100, limit = 5)
            val companyId = company.id
            val featureId = feature.getIdOrThrow()

            whenever(
                usageRepository.countByCompanyIdAndFeatureIdAndUsedAtBetween(
                    eq(companyId),
                    eq(featureId),
                    any(),
                    any()
                )
            ).thenReturn(6L)

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "excessive")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.FREQUENCY_LIMIT_EXCEEDED)
        }

        @Test
        fun failure_calculate_when_feature_id_is_null() {
            val company = company(500)
            val feature = mock<AiFeature> {
                on { getIdOrThrow() } doThrow GlobalException(ErrorCode.ID_INITIALIZE)
                on { usagePolicy } doReturn UsagePolicy(100, 9999, LimitUnit.PER_MONTH)
            }

            val exception = assertThrows<GlobalException> {
                calculator.calculate(company, feature, input = "invalid")
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
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