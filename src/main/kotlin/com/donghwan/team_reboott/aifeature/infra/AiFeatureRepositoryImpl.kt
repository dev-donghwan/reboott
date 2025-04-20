package com.donghwan.team_reboott.aifeature.infra

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeature.domain.repository.AiFeatureRepository
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.springframework.stereotype.Component

@Component
class AiFeatureRepositoryImpl(
    private val jpaRepository: AiFeatureJpaRepository
) : AiFeatureRepository {

    override fun save(entity: AiFeature): AiFeature = jpaRepository.save(entity)

    override fun getById(id: Long): AiFeature = jpaRepository.findById(id).orElseThrow { throw GlobalException(errorCode = ErrorCode.ENTITY_NOT_FOUND, message = "AiFeature(id=$id) not found.") }

    override fun getAll(): List<AiFeature> = jpaRepository.findAll()

    override fun getAllByIds(ids: List<Long>): List<AiFeature> = jpaRepository.findAllById(ids)

}