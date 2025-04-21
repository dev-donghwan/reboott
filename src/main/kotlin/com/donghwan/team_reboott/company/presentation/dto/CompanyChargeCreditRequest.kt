package com.donghwan.team_reboott.company.presentation.dto

data class CompanyChargeCreditRequest(
    val amount: Long
) {
    init {
        require(amount > 0) { "Amount must be greater than 0" }
    }
}
