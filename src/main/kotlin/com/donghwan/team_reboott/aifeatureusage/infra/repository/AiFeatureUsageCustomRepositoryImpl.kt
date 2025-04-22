package com.donghwan.team_reboott.aifeatureusage.infra.repository

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.aifeatureusage.domain.model.AiFeatureUsage
import com.donghwan.team_reboott.company.domain.model.Company
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicate
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AiFeatureUsageCustomRepositoryImpl(
    private val entityManager: EntityManager
) : AiFeatureUsageCustomRepository {

    private val renderer = JpqlRenderer()
    private val context = JpqlRenderContext()

    override fun findUsageByCompanyIdAndDateRange(
        companyId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto> {
        return executeQuery(companyId, null, start, end, pageable)
    }

    override fun findUsageByCompanyIdAndFeatureIdAndDateRange(
        companyId: Long,
        featureId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto> {
        return executeQuery(companyId, featureId, start, end, pageable)
    }

    private fun executeQuery(
        companyId: Long,
        featureId: Long?,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<AiFeatureUsageProjectionDto> {
        val query = jpql {
            selectNew<AiFeatureUsageProjectionDto>(
                path(AiFeatureUsage::company).path(Company::_id),
                path(AiFeatureUsage::company).path(Company::name),
                path(AiFeatureUsage::feature).path(AiFeature::id),
                path(AiFeatureUsage::feature).path(AiFeature::name),
                path(AiFeatureUsage::usedCredit),
                path(AiFeatureUsage::inputLength),
                path(AiFeatureUsage::usedAt),
            ).from(entity(AiFeatureUsage::class))
                .where(predicate(companyId, featureId, start, end))
        }

        val renderedQuery = renderer.render(query, context)
        val content = entityManager.createQuery(renderedQuery.query, AiFeatureUsageProjectionDto::class.java)
            .apply {
                renderedQuery.params.forEach { (k, v) -> setParameter(k, v) }
                firstResult = pageable.offset.toInt()
                maxResults = pageable.pageSize
            }.resultList

        val countQuery = jpql {
            select(count(entity(AiFeatureUsage::class)))
                .from(entity(AiFeatureUsage::class))
                .where(predicate(companyId, featureId, start, end))
        }

        val renderedCount = renderer.render(countQuery, context)
        val total = entityManager.createQuery(renderedCount.query, Long::class.java)
            .apply {
                renderedCount.params.forEach { (k, v) -> setParameter(k, v) }
            }.singleResult

        return PageImpl(content, pageable, total)
    }

    private fun Jpql.predicate(
        companyId: Long,
        featureId: Long? = null,
        start: LocalDateTime,
        end: LocalDateTime
    ): Predicate {
        return if (featureId == null) {
            and(
                path(AiFeatureUsage::company).path(Company::_id).equal(companyId),
                path(AiFeatureUsage::usedAt).between(start, end)
            )
        } else {
            and(
                path(AiFeatureUsage::company).path(Company::_id).equal(companyId),
                path(AiFeatureUsage::feature).path(AiFeature::id).equal(featureId),
                path(AiFeatureUsage::usedAt).between(start, end)
            )
        }
    }
}