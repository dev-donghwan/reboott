package com.donghwan.team_reboott.aifeature.domain.model

import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import jakarta.persistence.*

@Entity
class AiFeature(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    @Embedded
    val usagePolicy: UsagePolicy
) {

    fun getIdOrThrow(): Long = id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    companion object {
        fun create(name: String, costPerRequest: Int, limitPerUnit: Int, limitUnit: LimitUnit): AiFeature {
            return AiFeature(
                id = null,
                name = name,
                usagePolicy = UsagePolicy(
                    costPerRequest = costPerRequest,
                    limitPerUnit = limitPerUnit,
                    limitUnit = limitUnit
                )
            )
        }
    }
}