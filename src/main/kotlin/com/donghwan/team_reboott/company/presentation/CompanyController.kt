package com.donghwan.team_reboott.company.presentation

import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.SuccessCode
import com.donghwan.team_reboott.company.application.dto.ChargeCompanyCreditCommand
import com.donghwan.team_reboott.company.application.dto.UpdateCompanyBundleCommand
import com.donghwan.team_reboott.company.application.service.CompanyService
import com.donghwan.team_reboott.company.presentation.dto.CompanyBundleUpdateRequest
import com.donghwan.team_reboott.company.presentation.dto.CompanyChargeCreditRequest
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CompanyController(
    private val companyService: CompanyService
) {

    @PatchMapping("/v1/companies/{companyId}/bundle")
    fun updateBundle(
        @PathVariable companyId: Long,
        @RequestBody request: CompanyBundleUpdateRequest
    ): Result<Unit> {
        val command = UpdateCompanyBundleCommand(companyId, request.bundleId)
        companyService.changeBundle(command)
        return HttpResponse.success(SuccessCode.OK)
    }

    @PostMapping("/v1/companies/{companyId}/credits")
    fun chargeCredit(
        @PathVariable companyId: Long,
        @RequestBody request: CompanyChargeCreditRequest
    ): Result<Unit> {
        val command = ChargeCompanyCreditCommand(companyId, request.amount)
        companyService.chargeCredit(command)
        return HttpResponse.success(SuccessCode.OK)
    }
}