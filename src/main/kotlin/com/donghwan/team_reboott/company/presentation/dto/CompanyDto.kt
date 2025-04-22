package com.donghwan.team_reboott.company.presentation.dto

data class CompanyDto(
    val id:Long,
    val name: String,
    val credit: Long,
    val bundleId: Long?,
)
