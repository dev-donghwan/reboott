package com.donghwan.team_reboott.aifeature.application.service

import com.donghwan.team_reboott.aifeature.application.dto.UseFeatureCommand
import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import com.donghwan.team_reboott.company.infra.jpa.CompanyCreditJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class AiFeatureServiceConcurrencyTest @Autowired constructor(
    private val aiFeatureService: AiFeatureService,
    private val aiFeatureRepository: AiFeatureRepository,
    private val companyRepository: CompanyRepository,
    private val creditRepository: CompanyCreditJpaRepository,
    private val bundleRepository: AiFeatureBundleRepository
) {

    @Test
    fun success_concurrent_useFeature() {
        // given
        val credit = creditRepository.save(CompanyCredit.create(10_000))
        val feature = aiFeatureRepository.save(
            AiFeature.create("Chat", 100, 10_000, LimitUnit.PER_REQUEST)
        )
        val bundle = bundleRepository.save(
            AiFeatureBundle.create("Standard", listOf(feature))
        )
        val company = companyRepository.save(
            Company.create("Reboott", credit).also { it.assignBundle(bundle) }
        )

        val threadCount = 20
        val command = UseFeatureCommand(
            companyId = company.id,
            featureId = feature.getIdOrThrow(),
            input = "hello"
        )

        // when
        val futures = (1..threadCount).map {
            CompletableFuture.runAsync {
                aiFeatureService.useFeature(command)
            }
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()

        // then
        val updated = companyRepository.getById(company.id)
        val expected = 10_000L - (100L * threadCount)
        assertThat(updated.credit.amount).isEqualTo(expected)
    }

    @Test
    fun success_concurrent_useFeature_when_credit_insufficient() {
        // given
        val costPerRequest = 100L
        val allowedAttempts = 5

        val credit = creditRepository.save(CompanyCredit.create(costPerRequest * allowedAttempts))
        val feature = aiFeatureRepository.save(
            AiFeature.create("Chat", costPerRequest.toInt(), 1000, LimitUnit.PER_REQUEST)
        )
        val bundle = bundleRepository.save(
            AiFeatureBundle.create("Standard", listOf(feature))
        )
        val company = companyRepository.save(
            Company.create("Reboott", credit).also { it.assignBundle(bundle) }
        )

        val threadCount = 10  // 5개는 성공, 5개는 실패 예상
        val command = UseFeatureCommand(
            companyId = company.id,
            featureId = feature.getIdOrThrow(),
            input = "hi"
        )

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // when
        val futures = (1..threadCount).map {
            CompletableFuture.runAsync {
                try {
                    aiFeatureService.useFeature(command)
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failureCount.incrementAndGet()
                }
            }
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()

        // then
        val updated = companyRepository.getById(company.id)

        assertThat(successCount.get()).isEqualTo(allowedAttempts)
        assertThat(failureCount.get()).isEqualTo(threadCount - allowedAttempts)
        assertThat(updated.credit.amount).isEqualTo(0L)
    }

    @Test
    fun failure_useFeature_when_feature_not_in_bundle() {
        // given
        val credit = creditRepository.save(CompanyCredit.create(1000))
        val feature = aiFeatureRepository.save(
            AiFeature.create("Chat", 100, 1000, LimitUnit.PER_REQUEST)
        )
        val bundle = bundleRepository.save(
            AiFeatureBundle.create("Empty", emptyList()) // 👈 feature 미포함
        )
        val company = companyRepository.save(
            Company.create("NoAccessCompany", credit).also { it.assignBundle(bundle) }
        )

        val command = UseFeatureCommand(
            companyId = company.id,
            featureId = feature.getIdOrThrow(),
            input = "hello"
        )

        // when
        val exception = org.junit.jupiter.api.assertThrows<GlobalException> {
            aiFeatureService.useFeature(command)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.FEATURE_NOT_AVAILABLE)
    }
}