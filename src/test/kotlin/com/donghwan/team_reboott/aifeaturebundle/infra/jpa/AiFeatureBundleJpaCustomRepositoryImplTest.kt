package com.donghwan.team_reboott.aifeaturebundle.infra.jpa

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeature.infra.AiFeatureRepositoryImpl
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.aifeaturebundle.infra.AiFeatureBundleRepositoryImpl
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@Import(
    AiFeatureRepositoryImpl::class,
    AiFeatureBundleRepositoryImpl::class
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AiFeatureBundleJpaCustomRepositoryImplTest @Autowired constructor(
    val entityManager: EntityManager,
    val featureRepository: AiFeatureRepository,
    val bundleRepository: AiFeatureBundleRepository
) {

    private val customRepository = AiFeatureBundleJpaCustomRepositoryImpl(entityManager)

    private fun createFeature(name: String): AiFeature {
        return AiFeature.create(
            name = name,
            costPerRequest = 10,
            limitPerUnit = 1000,
            limitUnit = LimitUnit.PER_REQUEST
        )
    }

    @Test
    fun success_returnsTrue_whenExactFeatureSetExists() {
        // given
        val f1 = featureRepository.save(createFeature("f1"))
        val f2 = featureRepository.save(createFeature("f2"))
        val bundle = AiFeatureBundle.create("basic", listOf(f1, f2))
        bundleRepository.save(bundle)

        // when
        val exists = customRepository.existsFeatureSet(setOf(f1.getIdOrThrow(), f2.getIdOrThrow()))

        // then
        assertThat(exists).isTrue()
    }

    @Test
    fun success_returnsFalse_whenFeatureSetIsPartialMismatch() {
        val f1 = featureRepository.save(createFeature("f1"))
        val f2 = featureRepository.save(createFeature("f2"))
        val f3 = featureRepository.save(createFeature("f3"))
        val bundle = AiFeatureBundle.create("basic", listOf(f1, f2))
        bundleRepository.save(bundle)

        val exists = customRepository.existsFeatureSet(setOf(f1.getIdOrThrow(), f3.getIdOrThrow()))
        assertThat(exists).isFalse()
    }

    @Test
    fun success_returnsFalse_whenFeatureSetIsEmpty() {
        val exists = customRepository.existsFeatureSet(emptySet())
        assertThat(exists).isFalse()
    }
}