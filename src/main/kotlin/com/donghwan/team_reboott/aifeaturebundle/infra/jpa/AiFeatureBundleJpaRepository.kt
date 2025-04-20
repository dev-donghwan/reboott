package com.donghwan.team_reboott.aifeaturebundle.infra.jpa

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface AiFeatureBundleJpaRepository : JpaRepository<AiFeatureBundle, Long>, AiFeatureBundleJpaCustomRepository {

    @EntityGraph(attributePaths = ["mappings", "mappings.feature"])
    override fun findById(@Param("id") id: Long): Optional<AiFeatureBundle>

    @EntityGraph(attributePaths = ["mappings", "mappings.feature"])
    override fun findAll(pageable: Pageable): Page<AiFeatureBundle>

    fun existsByName(name: String): Boolean

}