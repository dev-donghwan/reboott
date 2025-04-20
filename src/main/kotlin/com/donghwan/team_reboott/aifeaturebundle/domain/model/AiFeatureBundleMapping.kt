package com.donghwan.team_reboott.aifeaturebundle.domain.model

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import jakarta.persistence.*

@Entity
class AiFeatureBundleMapping private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val _id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id")
    val bundle: AiFeatureBundle,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_feature_id")
    val feature: AiFeature
) {
    val id: Long
        get() = _id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    companion object {
        fun create(bundle: AiFeatureBundle, feature: AiFeature): AiFeatureBundleMapping {
            return AiFeatureBundleMapping(null, bundle, feature)
        }
    }
}