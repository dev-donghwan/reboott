package com.donghwan.team_reboott.aifeatureusage.domain

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AiFeatureUsageTest {

    private val dummyCompany = Company.create("Reboott", CompanyCredit.create(1000))
    private val dummyFeature = AiFeature.create("Chat", 100, 1000, LimitUnit.PER_REQUEST)

    @Nested
    inner class Create {

        @Test
        fun success_create_with_valid_values() {
            val usage = AiFeatureUsage.create(dummyCompany, dummyFeature, usedCredit = 100, inputLength = 5)

            assertThat(usage.company).isEqualTo(dummyCompany)
            assertThat(usage.feature).isEqualTo(dummyFeature)
            assertThat(usage.usedCredit).isEqualTo(100)
            assertThat(usage.inputLength).isEqualTo(5)
            assertThat(usage.usedAt).isNotNull()
        }

        @Test
        fun failure_create_when_usedCredit_is_negative() {
            val exception = assertThrows<GlobalException> {
                AiFeatureUsage.create(dummyCompany, dummyFeature, usedCredit = -1, inputLength = 10)
            }

            assertThat(exception.message).contains("Used credit must not be negative")
            assertThat(exception.errorCode).isEqualTo(ErrorCode.INVALID_PARAM)
        }

        @Test
        fun failure_create_when_inputLength_is_negative() {
            val exception = assertThrows<GlobalException> {
                AiFeatureUsage.create(dummyCompany, dummyFeature, usedCredit = 100, inputLength = -5)
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.INVALID_PARAM)
            assertThat(exception.message).contains("Input length must not be negative")
        }
    }

    @Nested
    inner class GetId {

        @Test
        fun failure_get_id_when_not_initialized() {
            val usage = AiFeatureUsage.create(dummyCompany, dummyFeature, 100, 10)
            val exception = assertThrows<GlobalException> {
                usage.id
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
        }
    }
}