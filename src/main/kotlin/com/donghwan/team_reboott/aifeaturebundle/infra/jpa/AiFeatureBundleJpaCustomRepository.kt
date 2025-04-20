package com.donghwan.team_reboott.aifeaturebundle.infra.jpa

interface AiFeatureBundleJpaCustomRepository {

    fun existsFeatureSet(ids:Set<Long>):Boolean

}