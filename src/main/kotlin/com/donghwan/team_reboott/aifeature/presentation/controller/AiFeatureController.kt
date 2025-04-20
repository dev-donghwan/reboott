package com.donghwan.team_reboott.aifeature.presentation.controller

import com.donghwan.team_reboott.aifeature.application.service.AiFeatureService
import com.donghwan.team_reboott.aifeature.presentation.dto.response.AiFeatureListResponse
import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.SuccessCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AiFeatureController(
    private val aiFeatureService: AiFeatureService
) {

    @GetMapping("/v1/ai-features")
    fun getAllFeatures(): Result<AiFeatureListResponse> {
        val features = AiFeatureListResponse(aiFeatureService.findAll())
        return HttpResponse.success(
            code = SuccessCode.OK,
            data = features
        )
    }
}