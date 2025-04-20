package com.donghwan.team_reboott.aifeaturebundle.domain.model

import com.donghwan.team_reboott.aifeature.domain.model.AiFeature
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import jakarta.persistence.*

@Entity
class AiFeatureBundle private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val _id: Long? = null,

    val name: String,

    @OneToMany(mappedBy = "bundle", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    private val mappings: MutableList<AiFeatureBundleMapping> = mutableListOf()
) {
    val id: Long
        get() = _id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    fun hasFeature(featureId: Long): Boolean = mappings.any { it.feature.id == featureId }

    fun getFeatures(): List<AiFeature> = mappings.map { it.feature }

    companion object {
        fun create(name: String, features: List<AiFeature>): AiFeatureBundle {
            val bundle = AiFeatureBundle(name = name)
            features.forEach { feature ->
                bundle.mappings.add(AiFeatureBundleMapping.create(bundle, feature))
            }
            return bundle
        }
    }
}
