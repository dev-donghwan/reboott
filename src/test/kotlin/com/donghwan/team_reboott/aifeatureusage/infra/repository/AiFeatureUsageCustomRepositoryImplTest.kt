package com.donghwan.team_reboott.aifeatureusage.infra.repository

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.model.LimitUnit
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeature.infra.AiFeatureRepositoryImpl
import com.donghwan.team_reboott.aifeaturebundle.infra.AiFeatureBundleRepositoryImpl
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import com.donghwan.team_reboott.company.infra.CompanyRepositoryImpl
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@Import(
    AiFeatureRepositoryImpl::class,
    AiFeatureBundleRepositoryImpl::class,
    AiFeatureUsageRepositoryImpl::class,
    CompanyRepositoryImpl::class
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AiFeatureUsageCustomRepositoryImplTest @Autowired constructor(
    private val entityManager: EntityManager,
    private val usageRepository: AiFeatureUsageRepository,
    private val featureRepository: AiFeatureRepository,
    private val companyRepository: CompanyRepository,
) {

    private val customRepository = AiFeatureUsageCustomRepositoryImpl(entityManager)

    private fun persistCompany(name: String = "testCompany"): Company {
        val credit = CompanyCredit.create(1000)
        entityManager.persist(credit)
        return companyRepository.save(Company.create(name, credit))
    }

    private fun persistFeature(name: String = "featureA"): AiFeature {
        return featureRepository.save(
            AiFeature.create(name, 10, 100, LimitUnit.PER_REQUEST)
        )
    }

    private fun persistUsage(company: Company, feature: AiFeature): AiFeatureUsage {
        return usageRepository.save(
            AiFeatureUsage.create(company, feature, 50, 500)
        )
    }

    @Nested
    @DisplayName("findUsageByCompanyIdAndDateRange")
    inner class FindByCompanyAndDate {

        @Test
        fun success_withData() {
            val company = persistCompany()
            val feature = persistFeature()
            val now = LocalDateTime.now()
            persistUsage(company, feature)

            val result = customRepository.findUsageByCompanyIdAndDateRange(
                companyId = company.id!!,
                start = now.minusDays(1),
                end = now.plusDays(1),
                pageable = PageRequest.of(0, 10)
            )

            assertThat(result.content).hasSize(1)
            assertThat(result.totalElements).isEqualTo(1)
        }

        @Test
        fun success_noData() {
            val company = persistCompany()
            val now = LocalDateTime.now()

            val result = customRepository.findUsageByCompanyIdAndDateRange(
                companyId = company.id!!,
                start = now.minusDays(1),
                end = now.plusDays(1),
                pageable = PageRequest.of(0, 10)
            )

            assertThat(result.content).isEmpty()
            assertThat(result.totalElements).isEqualTo(0)
        }
    }

    @Nested
    @DisplayName("findUsageByCompanyIdAndFeatureIdAndDateRange")
    inner class FindByCompanyFeatureAndDate {

        @Test
        fun success_withData() {
            val company = persistCompany()
            val feature = persistFeature()
            val now = LocalDateTime.now()
            persistUsage(company, feature)

            val result = customRepository.findUsageByCompanyIdAndFeatureIdAndDateRange(
                companyId = company.id!!,
                featureId = feature.id!!,
                start = now.minusDays(1),
                end = now.plusDays(1),
                pageable = PageRequest.of(0, 10)
            )

            assertThat(result.content).hasSize(1)
            assertThat(result.totalElements).isEqualTo(1)
        }

        @Test
        fun success_noData() {
            val company = persistCompany()
            val feature = persistFeature()
            val now = LocalDateTime.now()

            val result = customRepository.findUsageByCompanyIdAndFeatureIdAndDateRange(
                companyId = company.id!!,
                featureId = feature.id!!,
                start = now.minusDays(1),
                end = now.plusDays(1),
                pageable = PageRequest.of(0, 10)
            )

            assertThat(result.content).isEmpty()
            assertThat(result.totalElements).isEqualTo(0)
        }
    }
}