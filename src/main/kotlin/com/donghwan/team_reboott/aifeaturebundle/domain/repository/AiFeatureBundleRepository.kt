package com.donghwan.team_reboott.aifeaturebundle.domain.repository

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AiFeatureBundleRepository {

    fun save(bundle: AiFeatureBundle): AiFeatureBundle

    fun existsFeatureSet(ids: Set<Long>): Boolean

    fun existsName(name: String): Boolean

    fun getById(id: Long): AiFeatureBundle

    fun getAll(pageable: Pageable): Page<AiFeatureBundle>
}