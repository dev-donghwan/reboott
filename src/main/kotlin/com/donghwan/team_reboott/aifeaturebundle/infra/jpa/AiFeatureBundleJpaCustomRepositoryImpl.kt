package com.donghwan.team_reboott.aifeaturebundle.infra.jpa

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundleMapping
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class AiFeatureBundleJpaCustomRepositoryImpl(
    private val entityManager: EntityManager
) : AiFeatureBundleJpaCustomRepository {

    override fun existsFeatureSet(ids: Set<Long>): Boolean {
        if (ids.isEmpty()) return false

        val sql = """
        SELECT bundle_id
        FROM ai_feature_bundle_mapping
        GROUP BY bundle_id
        HAVING COUNT(*) = :size
            AND COUNT(CASE WHEN ai_feature_id IN (:ids) THEN 1 END) = :size
            AND COUNT(DISTINCT ai_feature_id) = :size
    """.trimIndent()

        val query = entityManager.createNativeQuery(sql)
        query.setParameter("size", ids.size)
        query.setParameter("ids", ids)

        val result = query.resultList
        return result.isNotEmpty()
    }
}