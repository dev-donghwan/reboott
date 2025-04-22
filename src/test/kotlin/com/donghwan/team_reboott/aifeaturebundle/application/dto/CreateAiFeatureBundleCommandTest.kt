package com.donghwan.team_reboott.aifeaturebundle.application.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateAiFeatureBundleCommandTest {

    @Nested
    inner class InitValidation {

        @Test
        fun success_create_with_valid_data() {
            val command = CreateAiFeatureBundleCommand(
                name = "Standard Bundle",
                featureIds = setOf(1L, 2L)
            )
            assertThat(command.name).isEqualTo("Standard Bundle")
            assertThat(command.featureIds).containsExactlyInAnyOrder(1L, 2L)
        }

        @Test
        fun failure_create_when_name_is_blank() {
            val exception = assertThrows<IllegalArgumentException> {
                CreateAiFeatureBundleCommand(
                    name = "  ",
                    featureIds = setOf(1L)
                )
            }
            assertThat(exception.message).contains("Name cannot be blank")
        }

        @Test
        fun failure_create_when_featureIds_is_empty() {
            val exception = assertThrows<IllegalArgumentException> {
                CreateAiFeatureBundleCommand(
                    name = "Bundle",
                    featureIds = emptySet()
                )
            }
            assertThat(exception.message).contains("Ids cannot be empty")
        }
    }
}