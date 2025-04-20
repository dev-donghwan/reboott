package com.donghwan.team_reboott.aifeature.domain.repository

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature

interface AiFeatureRepository {

    fun save(entity: AiFeature): AiFeature

    fun getById(id: Long): AiFeature

    fun getAll(): List<AiFeature>

    fun getAllByIds(ids: List<Long>): List<AiFeature>
}