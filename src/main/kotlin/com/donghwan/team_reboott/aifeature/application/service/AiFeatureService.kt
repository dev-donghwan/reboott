package com.donghwan.team_reboott.aifeature.application.service

import com.donghwan.team_reboott.aifeature.application.dto.UseFeatureCommand
import com.donghwan.team_reboott.aifeature.domain.policy.FeatureUsageCalculatorRouter
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeature.presentation.dto.AiFeatureDto
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.lock.DistributedLock
import com.donghwan.team_reboott.common.lock.LockKey
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AiFeatureService(
    // Lock
    private val distributedLock: DistributedLock,
    // Strategy
    private val calculatorRouter: FeatureUsageCalculatorRouter,
    // Repository
    private val aiFeatureRepository: AiFeatureRepository,
    private val companyRepository: CompanyRepository,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<AiFeatureDto> {
        return aiFeatureRepository.getAll()
            .map {
                AiFeatureDto(
                    id = it.getIdOrThrow(),
                    name = it.name,
                    costPerRequest = it.usagePolicy.costPerRequest,
                    limitPerUnit = it.usagePolicy.limitPerUnit,
                    limitUnit = it.usagePolicy.limitUnit.name
                )
            }.toList()
    }

    @Transactional
    fun useFeature(command: UseFeatureCommand) {
        distributedLock.lock(LockKey.companyCredit(command.companyId)) {

            val findFeature = aiFeatureRepository.getById(command.featureId)

            val findCompany = companyRepository.getById(command.companyId)

            if (!findCompany.bundle.hasFeature(findFeature.getIdOrThrow())) {
                throw GlobalException(ErrorCode.FEATURE_NOT_AVAILABLE)
            }

            val calculator = calculatorRouter.get(findFeature.usagePolicy.limitUnit)
            val requiredCredit = calculator.calculate(findCompany, findFeature, command.input)
            findCompany.credit.use(requiredCredit)

            // TODO
            // CompanyFeatureUsage(company, feature, LocalDateTime.now())
        }
    }
}