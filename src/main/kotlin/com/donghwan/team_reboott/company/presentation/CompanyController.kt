package com.donghwan.team_reboott.company.presentation

import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.dto.PageRequest
import com.donghwan.team_reboott.common.dto.PageResponse
import com.donghwan.team_reboott.common.dto.toPageResponse
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.SuccessCode
import com.donghwan.team_reboott.company.application.dto.ChargeCompanyCreditCommand
import com.donghwan.team_reboott.company.application.dto.UpdateCompanyBundleCommand
import com.donghwan.team_reboott.company.application.service.CompanyService
import com.donghwan.team_reboott.company.presentation.dto.CompanyBundleUpdateRequest
import com.donghwan.team_reboott.company.presentation.dto.CompanyChargeCreditRequest
import com.donghwan.team_reboott.company.presentation.dto.CompanyDto
import org.springframework.web.bind.annotation.*

@RestController
class CompanyController(
    private val companyService: CompanyService
) {
    @GetMapping("/v1/companies")
    fun findAllCompanies(request: PageRequest): Result<PageResponse<CompanyDto>> {
        val pageable = request.toPageable()
        val findAll = companyService.findAll(pageable)
        return HttpResponse.success(code = SuccessCode.OK, data = findAll.toPageResponse())
    }

    @PatchMapping("/v1/companies/{companyId}/bundles")
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