package com.donghwan.team_reboott.company.domain.model

import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompanyCreditTest {

    @Nested
    inner class Use {

        @Test
        fun success_use_when_sufficient_credit() {
            val credit = CompanyCredit.create(1000)
            credit.use(300)
            assertThat(credit.amount).isEqualTo(700)
        }

        @Test
        fun failure_use_when_amount_is_zero_or_negative() {
            val credit = CompanyCredit.create(1000)
            val exception = assertThrows<IllegalArgumentException> {
                credit.use(0)
            }
            assertThat(exception.message).contains("greater than zero")
        }

        @Test
        fun failure_use_when_insufficient_credit() {
            val credit = CompanyCredit.create(100)
            val exception = assertThrows<GlobalException> {
                credit.use(200)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_ENOUGH_CREDIT)
        }
    }

    @Nested
    inner class CanUse {

        @Test
        fun success_can_use_when_sufficient_credit() {
            val credit = CompanyCredit.create(500)
            credit.canUse(500) // 예외 없음
        }

        @Test
        fun failure_can_use_when_insufficient_credit() {
            val credit = CompanyCredit.create(300)
            val exception = assertThrows<GlobalException> {
                credit.canUse(400)
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_ENOUGH_CREDIT)
        }
    }

    @Nested
    inner class Charge {

        @Test
        fun success_charge_with_valid_amount() {
            val credit = CompanyCredit.create(100)
            credit.charge(200)
            assertThat(credit.amount).isEqualTo(300)
        }

        @Test
        fun failure_charge_with_zero_or_negative_amount() {
            val credit = CompanyCredit.create(100)
            val exception = assertThrows<IllegalArgumentException> {
                credit.charge(0)
            }
            assertThat(exception.message).contains("greater than zero")
        }
    }

    @Nested
    inner class GetId {

        @Test
        fun failure_get_id_when_not_initialized() {
            val credit = CompanyCredit.create(100)
            val exception = assertThrows<GlobalException> {
                credit.id
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
        }
    }
}