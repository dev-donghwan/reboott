package com.donghwan.team_reboott.aifeature.application.service

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.policy.FeatureUsageCalculatorRouter
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeatureusage.application.AiFeatureUsageService
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.lock.DistributedLock
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AiFeatureServiceTest {

    private val distributedLock: DistributedLock = mock()
    private val calculatorRouter: FeatureUsageCalculatorRouter = mock()
    private val featureUsageService: AiFeatureUsageService = mock()
    private val aiFeatureRepository: AiFeatureRepository = mock()
    private val companyRepository: CompanyRepository = mock()
    private val service = AiFeatureService(distributedLock, calculatorRouter, featureUsageService, aiFeatureRepository, companyRepository)

    @Nested
    inner class FindAll {

        @Test
        fun success_with_data() {
            // given
            val list = listOf(
                AiFeature.create("Chat", 100, 1000, LimitUnit.PER_REQUEST).copyWithId(1L),
                AiFeature.create("Translate", 200, 2000, LimitUnit.PER_MONTH).copyWithId(2L)
            )

            whenever(aiFeatureRepository.getAll()).thenReturn(list)

            // when
            val result = service.findAll()

            // then
            assertThat(result).hasSize(2)
            assertThat(result[0].name).isEqualTo("Chat")
            assertThat(result[1].name).isEqualTo("Translate")
        }

        @Test
        fun success_empty_list() {
            // given
            whenever(aiFeatureRepository.getAll()).thenReturn(emptyList())

            // when
            val result = service.findAll()

            // then
            assertThat(result).isEmpty()
        }

        @Test
        fun failure_id_not_initialized() {
            // given
            val list = listOf(
                AiFeature.create("Chat", 100, 1000, LimitUnit.PER_REQUEST)
            )

            whenever(aiFeatureRepository.getAll()).thenReturn(list)

            // when
            val exception = assertThrows<GlobalException> {
                service.findAll()
            }

            // then
            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
        }
    }

    private fun AiFeature.copyWithId(id: Long): AiFeature {
        return AiFeature(
            id = id,
            name = this.name,
            usagePolicy = this.usagePolicy,
        )
    }
}