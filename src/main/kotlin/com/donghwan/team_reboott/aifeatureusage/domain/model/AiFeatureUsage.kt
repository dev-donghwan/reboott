package com.donghwan.team_reboott.aifeatureusage.domain.model

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.company.domain.model.Company
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Access(AccessType.FIELD)
class AiFeatureUsage private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val _id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val company: Company,

    @ManyToOne(fetch = FetchType.LAZY)
    val feature: AiFeature,

    val usedCredit: Long,

    val inputLength: Int,

    val usedAt: LocalDateTime = LocalDateTime.now()
) {
    val id: Long
        get() = _id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    companion object {
        fun create(company: Company, feature: AiFeature, usedCredit: Long, inputLength: Int): AiFeatureUsage {
            if (usedCredit < 0) throw GlobalException(ErrorCode.INVALID_PARAM, "Used credit must not be negative")
            if (inputLength < 0) throw GlobalException(ErrorCode.INVALID_PARAM, "Input length must not be negative")

            return AiFeatureUsage(
                company = company,
                feature = feature,
                usedCredit = usedCredit,
                inputLength = inputLength
            )
        }
    }
}