package com.donghwan.team_reboott.company.application.service

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.common.lock.DistributedLock
import com.donghwan.team_reboott.company.application.dto.UpdateCompanyBundleCommand
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CompanyServiceTest {

    private val distributedLock = mock<DistributedLock>()
    private val bundleRepository = mock<AiFeatureBundleRepository>()
    private val companyRepository = mock<CompanyRepository>()

    private val service = CompanyService(
        distributedLock = distributedLock,
        bundleRepository = bundleRepository,
        companyRepository = companyRepository,
    )

    @Nested
    inner class ChangeBundle {

        @Test
        fun success_changeBundle() {
            // given
            val company = Company.create("test-company", mock())
            val bundle = AiFeatureBundle.create("test-bundle", listOf())
            val command = UpdateCompanyBundleCommand(companyId = 1L, bundleId = 2L)

            whenever(companyRepository.getByIdWithBundle(1L)).thenReturn(company)
            whenever(bundleRepository.getById(2L)).thenReturn(bundle)

            // when
            service.changeBundle(command)

            // then
            assertThat(company.bundle).isEqualTo(bundle)
        }

        @Test
        fun failure_changeBundle_when_company_not_found() {
            // given
            val command = UpdateCompanyBundleCommand(companyId = 1L, bundleId = 2L)
            val bundle = AiFeatureBundle.create("test-bundle", listOf())

            whenever(bundleRepository.getById(2L)).thenReturn(bundle)
            whenever(companyRepository.getByIdWithBundle(1L)).thenThrow(EntityNotFoundException("not found"))

            // expect
            assertThrows<EntityNotFoundException> {
                service.changeBundle(command)
            }
        }

        @Test
        fun failure_changeBundle_when_bundle_not_found() {
            // given
            val command = UpdateCompanyBundleCommand(companyId = 1L, bundleId = 2L)
            val company = Company.create("test-company", mock())

            whenever(companyRepository.getById(1L)).thenReturn(company)
            whenever(bundleRepository.getById(2L)).thenThrow(EntityNotFoundException("not found"))

            // expect
            assertThrows<EntityNotFoundException> {
                service.changeBundle(command)
            }
        }
    }
}