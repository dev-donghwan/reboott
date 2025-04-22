package com.donghwan.team_reboott.company.application.service

import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.common.lock.DistributedLock
import com.donghwan.team_reboott.common.lock.LockKey
import com.donghwan.team_reboott.company.application.dto.ChargeCompanyCreditCommand
import com.donghwan.team_reboott.company.application.dto.UpdateCompanyBundleCommand
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import com.donghwan.team_reboott.company.presentation.dto.CompanyDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompanyService(
    // Lock
    private val distributedLock: DistributedLock,
    // Repository
    private val bundleRepository: AiFeatureBundleRepository,
    private val companyRepository: CompanyRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<CompanyDto> {
        return companyRepository.getAll(pageable).map {
            CompanyDto(
                id = it.id,
                name = it.name,
                credit = it.credit.amount,
                bundleId = if (it.hasBundle()) it.bundle.id else null
            )
        }
    }

    @Transactional
    fun changeBundle(command: UpdateCompanyBundleCommand) {
        val findBundle = bundleRepository.getById(command.bundleId)
        val findCompany = companyRepository.getById(command.companyId)
        findCompany.assignBundle(findBundle)

        companyRepository.save(findCompany)
    }

    @Transactional
    fun chargeCredit(command: ChargeCompanyCreditCommand) {
        distributedLock.lock(LockKey.companyCredit(command.companyId)) {
            val findCompany = companyRepository.getById(command.companyId)
            findCompany.credit.charge(command.chargeAmount)
        }
    }

}