package com.donghwan.team_reboott.aifeaturebundle.domain.model

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AiFeatureBundleTest {

    private fun dummyFeature(id: Long, name: String = "test"): AiFeature {
        return AiFeature.create(name, 100, 1000, LimitUnit.PER_REQUEST).copyWithId(id)
    }

    private fun AiFeature.copyWithId(id: Long): AiFeature {
        return AiFeature(id = id, name = this.name, usagePolicy = this.usagePolicy)
    }

    @Nested
    inner class Create {

        @Test
        fun success_create_with_features() {
            val feature1 = dummyFeature(1L)
            val feature2 = dummyFeature(2L)

            val bundle = AiFeatureBundle.create("Standard", listOf(feature1, feature2))

            assertThat(bundle.getFeatures()).containsExactly(feature1, feature2)
        }

        @Test
        fun success_create_with_empty_features() {
            val bundle = AiFeatureBundle.create("Empty", emptyList())
            assertThat(bundle.getFeatures()).isEmpty()
        }
    }

    @Nested
    inner class GetId {

        @Test
        fun failure_when_id_is_not_initialized() {
            val bundle = AiFeatureBundle.create("Test", emptyList())
            val exception = assertThrows<GlobalException> {
                val unused = bundle.id
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
        }
    }

    @Nested
    inner class HasFeature {

        @Test
        fun success_when_feature_id_exists() {
            val feature = dummyFeature(1L)
            val bundle = AiFeatureBundle.create("WithFeature", listOf(feature))
            assertThat(bundle.hasFeature(1L)).isTrue()
        }

        @Test
        fun success_when_feature_id_does_not_exist() {
            val feature = dummyFeature(1L)
            val bundle = AiFeatureBundle.create("WithFeature", listOf(feature))
            assertThat(bundle.hasFeature(2L)).isFalse()
        }
    }

    @Nested
    inner class GetFeatures {

        @Test
        fun success_return_all_features() {
            val features = listOf(dummyFeature(1L), dummyFeature(2L))
            val bundle = AiFeatureBundle.create("Bundle", features)
            assertThat(bundle.getFeatures()).containsExactlyElementsOf(features)
        }

        @Test
        fun success_return_empty_list_when_no_features() {
            val bundle = AiFeatureBundle.create("Bundle", emptyList())
            assertThat(bundle.getFeatures()).isEmpty()
        }
    }
}