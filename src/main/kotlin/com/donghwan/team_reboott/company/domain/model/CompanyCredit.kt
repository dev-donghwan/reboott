package com.donghwan.team_reboott.company.domain.model

import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import jakarta.persistence.*

@Entity
class CompanyCredit private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val _id: Long? = null,
    @Column(name = "amount")
    private var _amount: Long
) {
    val id: Long
        get() = _id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    val amount: Long
        get() = _amount;

    fun canUse(cost: Long) {
        require(amount >= cost) { throw GlobalException(ErrorCode.NOT_ENOUGH_CREDIT) }
    }

    fun use(usageAmount: Long) {
        require(usageAmount > 0) { "Usage amount must be greater than zero." }
        if (_amount < usageAmount) throw GlobalException(ErrorCode.NOT_ENOUGH_CREDIT, "Not enough credit.")
        _amount -= usageAmount
    }

    fun charge(chargeAmount: Long) {
        require(chargeAmount > 0) { "Charge amount must be greater than zero." }
        this._amount += chargeAmount
    }

    companion object {
        fun create(amount: Long = 0): CompanyCredit = CompanyCredit(null, amount)
    }
}