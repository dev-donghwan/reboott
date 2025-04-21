package com.donghwan.team_reboott.company.application.service

import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.common.lock.DistributedLock
import com.donghwan.team_reboott.common.lock.LockKey
import com.donghwan.team_reboott.company.application.dto.ChargeCompanyCreditCommand
import com.donghwan.team_reboott.company.application.dto.UpdateCompanyBundleCommand
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
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