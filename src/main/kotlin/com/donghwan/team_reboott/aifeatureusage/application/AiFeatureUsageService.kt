package com.donghwan.team_reboott.aifeatureusage.application

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeatureusage.application.dto.FindFeatureUsageCommand
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.aifeatureusage.domain.repository.AiFeatureUsageRepository
import com.donghwan.team_reboott.company.domain.model.Company
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AiFeatureUsageService(
    private val aiFeatureUsageRepository: AiFeatureUsageRepository
) {

    @Transactional
    fun create(company: Company, feature: AiFeature, usedCredit: Long, inputLength: Int): Long {
        val usage = AiFeatureUsage.create(company, feature, usedCredit, inputLength)
        return aiFeatureUsageRepository.save(usage).id
    }

    fun getUsage(
        command: FindFeatureUsageCommand,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto> {
        return if (command.hasFeatureId()) {
            aiFeatureUsageRepository.findByCompanyIdAndFeatureIdAndUsedAtBetween(
                companyId = command.companyId,
                featureId = command.featureId!!,
                start = command.start,
                end = command.end,
                pageable = pageable
            )
        } else {
            aiFeatureUsageRepository.findByCompanyIdAndUsedAtBetween(
                companyId = command.companyId,
                start = command.start,
                end = command.end,
                pageable = pageable
            )
        }
    }
}