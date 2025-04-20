package com.donghwan.team_reboott.aifeaturebundle.application.service

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.aifeature.presentation.dto.AiFeatureDto
import com.donghwan.team_reboott.aifeaturebundle.application.dto.CreateAiFeatureBundleCommand
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.aifeaturebundle.presentation.dto.AiFeatureBundleDto
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AiFeatureBundleService(
    private val featureRepository: AiFeatureRepository,
    private val bundleRepository: AiFeatureBundleRepository
) {

    @Transactional
    fun create(command: CreateAiFeatureBundleCommand): Long {
        val features: List<AiFeature> = featureRepository.getAllByIds(command.featureIds.toList())

        if (bundleRepository.existsName(command.name)) {
            throw GlobalException(ErrorCode.INVALID_PARAM, "The bundle name already exists: ${command.name}")
        }

        if (bundleRepository.existsFeatureSet(command.featureIds)) {
            throw GlobalException(ErrorCode.INVALID_PARAM, "A bundle with the same feature set already exists.")
        }

        val bundle = AiFeatureBundle.create(command.name, features)
        return bundleRepository.save(bundle).id
    }

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<AiFeatureBundleDto> {
        return bundleRepository.getAll(pageable).map {
            val features = it.getFeatures().map { feature ->
                AiFeatureDto(
                    id = feature.getIdOrThrow(),
                    name = feature.name,
                    costPerRequest = feature.usagePolicy.costPerRequest,
                    limitPerUnit = feature.usagePolicy.limitPerUnit,
                    limitUnit = feature.usagePolicy.limitUnit.name
                )
            }

            AiFeatureBundleDto(
                id = it.id,
                name = it.name,
                features = features
            )
        }
    }
}