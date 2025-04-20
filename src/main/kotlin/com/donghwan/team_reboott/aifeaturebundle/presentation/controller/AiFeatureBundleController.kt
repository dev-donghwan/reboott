package com.donghwan.team_reboott.aifeaturebundle.presentation.controller

import com.donghwan.team_reboott.aifeaturebundle.application.dto.CreateAiFeatureBundleCommand
import com.donghwan.team_reboott.aifeaturebundle.application.service.AiFeatureBundleService
import com.donghwan.team_reboott.aifeaturebundle.presentation.dto.AiFeatureBundleDto
import com.donghwan.team_reboott.aifeaturebundle.presentation.dto.request.AiFeatureBundleCreateRequest
import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.dto.PageRequest
import com.donghwan.team_reboott.common.dto.PageResponse
import com.donghwan.team_reboott.common.dto.toPageResponse
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.SuccessCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AiFeatureBundleController(
    private val aiFeatureBundleService: AiFeatureBundleService
) {

    @PostMapping("/v1/ai-bundles")
    fun create(
        @RequestBody request: AiFeatureBundleCreateRequest
    ): Result<Long> {
        val command = CreateAiFeatureBundleCommand(
            name = request.name,
            featureIds = request.featureIds
        )

        val id = aiFeatureBundleService.create(command)
        return HttpResponse.success(code = SuccessCode.CREATED, data = id)
    }

    @GetMapping("/v1/ai-bundles")
    fun getAll(request: PageRequest): Result<PageResponse<AiFeatureBundleDto>> {
        val toPageable = request.toPageable()

        val findPage = aiFeatureBundleService.findAll(toPageable)

        return HttpResponse.success(code = SuccessCode.OK, data = findPage.toPageResponse())
    }
}