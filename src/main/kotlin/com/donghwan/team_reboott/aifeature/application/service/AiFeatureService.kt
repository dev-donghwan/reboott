package com.donghwan.team_reboott.aifeature.application.service

import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeature.presentation.dto.AiFeatureDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AiFeatureService(
    private val aiFeatureRepository: AiFeatureRepository
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
}