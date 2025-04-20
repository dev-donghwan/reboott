package com.donghwan.team_reboott.aifeaturebundle.application.service

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.model.UsagePolicy
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeaturebundle.application.dto.CreateAiFeatureBundleCommand
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class AiFeatureBundleServiceTest {

    private val featureRepository = mock<AiFeatureRepository>()
    private val bundleRepository = mock<AiFeatureBundleRepository>()
    private val service = AiFeatureBundleService(featureRepository, bundleRepository)

    private fun createFeature(id: Long, name: String): AiFeature {
        val usagePolicy = mock<UsagePolicy> {
            on { costPerRequest } doReturn 10
            on { limitPerUnit } doReturn 1000
            on { limitUnit } doReturn LimitUnit.PER_REQUEST
        }

        return mock {
            on { this.id } doReturn id
            on { this.name } doReturn name
            on { this.usagePolicy } doReturn usagePolicy
            on { getIdOrThrow() } doReturn id
        }
    }

    @Nested
    inner class Create {

        @Test
        fun success_createBundle() {
            // given
            val f1 = createFeature(1L, "f1")
            val f2 = createFeature(2L, "f2")
            val command = CreateAiFeatureBundleCommand("basic", setOf(1L, 2L))

            whenever(featureRepository.getAllByIds(any())).thenReturn(listOf(f1, f2))
            whenever(bundleRepository.existsName(any())).thenReturn(false)
            whenever(bundleRepository.existsFeatureSet(any())).thenReturn(false)
            whenever(bundleRepository.save(any())).thenAnswer {
                mock<AiFeatureBundle> {
                    on { id } doReturn 100L
                }
            }

            // when
            val result = service.create(command)

            // then
            assertThat(result).isEqualTo(100L)
        }

        @Test
        fun failure_throwException_whenNameAlreadyExists() {
            // given
            val command = CreateAiFeatureBundleCommand("duplicated", setOf(1L, 2L))

            whenever(bundleRepository.existsName(command.name)).thenReturn(true)

            // when & then
            val exception = assertThrows<GlobalException> {
                service.create(command)
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.INVALID_PARAM)
            assertThat(exception.message).contains("already exists")
        }

        @Test
        fun failure_throwException_whenFeatureSetAlreadyExists() {
            // given
            val command = CreateAiFeatureBundleCommand("valid", setOf(1L, 2L))

            whenever(bundleRepository.existsName(command.name)).thenReturn(false)
            whenever(bundleRepository.existsFeatureSet(command.featureIds)).thenReturn(true)

            // when & then
            val exception = assertThrows<GlobalException> {
                service.create(command)
            }

            assertThat(exception.errorCode).isEqualTo(ErrorCode.INVALID_PARAM)
            assertThat(exception.message).contains("same feature set")
        }
    }

    @Nested
    inner class FindAll {
        @Test
        fun success_returnBundleDtoPage() {
            // given
            val feature = createFeature(1L, "f1")
            val bundle = mock<AiFeatureBundle> {
                on { id } doReturn 1L
                on { name } doReturn "basic"
                on { getFeatures() } doReturn listOf(feature)
            }
            val pageable = mock<Pageable>()
            val page = PageImpl(listOf(bundle))

            whenever(bundleRepository.getAll(pageable)).thenReturn(page)

            // when
            val result = service.findAll(pageable)

            // then
            assertThat(result.totalElements).isEqualTo(1)
            assertThat(result.content[0].name).isEqualTo("basic")
            assertThat(result.content[0].features[0].name).isEqualTo("f1")
        }
    }
}