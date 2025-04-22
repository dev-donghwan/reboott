package com.donghwan.team_reboott.aifeatureusage.presentation.controller

import AiFeatureUsageProjectionDto
import com.donghwan.team_reboott.aifeatureusage.application.AiFeatureUsageService
import com.donghwan.team_reboott.aifeatureusage.application.dto.FindFeatureUsageCommand
import com.donghwan.team_reboott.aifeatureusage.presentation.dto.FindFeatureUsageRequest
import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.dto.PageRequest
import com.donghwan.team_reboott.common.dto.PageResponse
import com.donghwan.team_reboott.common.dto.toPageResponse
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.SuccessCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

@RestController
class AiFeatureUsageController(
    private val usageService: AiFeatureUsageService
) {

    @GetMapping("/v1/ai-features/usages")
    fun getFeatureUsages(
        @ModelAttribute request: FindFeatureUsageRequest,
        @ModelAttribute pageRequest: PageRequest
    ): Result<PageResponse<AiFeatureUsageProjectionDto>> {
        val command = FindFeatureUsageCommand(
            companyId = request.companyId,
            featureId = request.featureId,
            start = request.startDate.atStartOfDay(),
            end = request.endDate.atTime(LocalTime.MAX)
        )

        return HttpResponse.success(
            code = SuccessCode.OK,
            data = usageService.getUsage(command, pageRequest.toPageable()).toPageResponse()
        )
    }
}