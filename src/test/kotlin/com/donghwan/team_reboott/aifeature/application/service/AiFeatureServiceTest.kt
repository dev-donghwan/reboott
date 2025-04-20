package com.donghwan.team_reboott.aifeature.application.service

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AiFeatureServiceTest {

    private val repository = mock<AiFeatureRepository>()
    private val service = AiFeatureService(repository)

    @Nested
    inner class FindAll {

        @Test
        fun success_with_data() {
            // given
            val list = listOf(
                AiFeature.create("Chat", 100, 1000, LimitUnit.PER_REQUEST).copyWithId(1L),
                AiFeature.create("Translate", 200, 2000, LimitUnit.PER_MONTH).copyWithId(2L)
            )

            whenever(repository.getAll()).thenReturn(list)

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
            whenever(repository.getAll()).thenReturn(emptyList())

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

            whenever(repository.getAll()).thenReturn(list)

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