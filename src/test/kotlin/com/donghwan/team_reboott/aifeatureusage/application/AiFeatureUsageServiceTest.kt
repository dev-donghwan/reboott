package com.donghwan.team_reboott.aifeatureusage.application

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeatureusage.application.dto.FindFeatureUsageCommand
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import com.donghwan.team_reboott.company.domain.model.Company
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

class AiFeatureUsageServiceTest {

    private val repository = mock<AiFeatureUsageRepository>()
    private val service = AiFeatureUsageService(repository)

    private val company = mock<Company>()
    private val feature = mock<AiFeature>()

    @Nested
    inner class Create {

        @Test
        fun success_create() {
            val usage = mock<AiFeatureUsage>()
            whenever(repository.save(any())).thenReturn(usage)
            whenever(usage.id).thenReturn(123L)

            val result = service.create(company, feature, 100, 200)

            assertThat(result).isEqualTo(123L)
        }
    }

    @Nested
    inner class GetUsage {

        private val now = LocalDateTime.now()
        private val pageable = PageRequest.of(0, 10)
        private val samplePage = PageImpl(listOf(mock<AiFeatureUsageProjectionDto>()))

        @Test
        fun success_with_featureId() {
            val command = FindFeatureUsageCommand(
                companyId = 1L,
                featureId = 2L,
                start = now.minusDays(1),
                end = now.plusDays(1),
            )

            whenever(repository.findByCompanyIdAndFeatureIdAndUsedAtBetween(1L, 2L, command.start, command.end, pageable))
                .thenReturn(samplePage)

            val result = service.getUsage(command, pageable)

            assertThat(result.content).hasSize(1)
        }

        @Test
        fun success_without_featureId() {
            val command = FindFeatureUsageCommand(
                companyId = 1L,
                featureId = null,
                start = now.minusDays(1),
                end = now.plusDays(1),
            )

            whenever(repository.findByCompanyIdAndUsedAtBetween(1L, command.start, command.end, pageable))
                .thenReturn(samplePage)

            val result = service.getUsage(command, pageable)

            assertThat(result.content).hasSize(1)
        }
    }
}