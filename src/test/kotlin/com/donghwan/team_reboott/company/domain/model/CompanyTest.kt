package com.donghwan.team_reboott.company.domain.model

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompanyTest {

    private val credit = CompanyCredit.create(1000)

    @Nested
    inner class Create {

        @Test
        fun success_create_company() {
            val company = Company.create("Reboott", credit)
            assertThat(company.name).isEqualTo("Reboott")
            assertThat(company.credit.amount).isEqualTo(1000)
        }
    }

    @Nested
    inner class AssignBundle {

        @Test
        fun success_assign_bundle_to_company() {
            val company = Company.create("Reboott", credit)
            val bundle = AiFeatureBundle.create("Standard", emptyList())

            company.assignBundle(bundle)

            assertThat(company.bundle).isEqualTo(bundle)
        }
    }

    @Nested
    inner class GetBundle {

        @Test
        fun success_get_bundle_when_assigned() {
            val bundle = AiFeatureBundle.create("Standard", emptyList())
            val company = Company.create("Reboott", credit)
            company.assignBundle(bundle)

            val result = company.bundle
            assertThat(result).isEqualTo(bundle)
        }

        @Test
        fun failure_get_bundle_when_not_assigned() {
            val company = Company.create("Reboott", credit)

            val exception = assertThrows<GlobalException> {
                company.bundle
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.ENTITY_NOT_ASSIGNED)
        }
    }

    @Nested
    inner class GetId {

        @Test
        fun failure_get_id_when_null() {
            val company = Company.create("Reboott", credit)

            val exception = assertThrows<GlobalException> {
                company.id
            }
            assertThat(exception.errorCode).isEqualTo(ErrorCode.ID_INITIALIZE)
        }
    }
}