package com.donghwan.team_reboott.aifeaturebundle.infra

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.repository.AiFeatureBundleRepository
import com.donghwan.team_reboott.aifeaturebundle.infra.jpa.AiFeatureBundleJpaRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class AiFeatureBundleRepositoryImpl(
    private val bundleJpaRepository: AiFeatureBundleJpaRepository
) : AiFeatureBundleRepository {

    override fun save(bundle: AiFeatureBundle): AiFeatureBundle = bundleJpaRepository.save(bundle)

    override fun existsFeatureSet(ids: Set<Long>): Boolean = bundleJpaRepository.existsFeatureSet(ids)

    override fun existsName(name: String): Boolean = bundleJpaRepository.existsByName(name)

    override fun getById(id: Long): AiFeatureBundle = bundleJpaRepository.findById(id).orElseThrow { throw GlobalException(ErrorCode.ENTITY_NOT_FOUND, "AiFeatureBundle(id: $id) not found") }

    override fun getAll(pageable: Pageable): Page<AiFeatureBundle> = bundleJpaRepository.findAll(pageable)
}