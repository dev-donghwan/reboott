package com.donghwan.team_reboott.aifeaturebundle.application.dto

data class CreateAiFeatureBundleCommand(
    val name: String,
    val featureIds: Set<Long>
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(featureIds.isNotEmpty()) { "featureIds cannot be empty" }
    }
}
