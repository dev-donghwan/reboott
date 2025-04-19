package com.donghwan.team_reboott.common.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

class ApiResponseTest {

    @Nested
    inner class Success {
        @Test
        fun success_normal() {
            val response = ApiResponse.success(SuccessCode.OK, data = "정상")

            assertThat(response.code).isEqualTo(SuccessCode.OK.code)
            assertThat(response.message).isEqualTo(SuccessCode.OK.message)
            assertThat(response.data).isEqualTo("정상")
        }

        @Test
        fun success_override_message() {
            val response = ApiResponse.success(SuccessCode.CREATED, message = "생성 완료", data = 123)

            assertThat(response.code).isEqualTo(SuccessCode.CREATED.code)
            assertThat(response.message).isEqualTo("생성 완료")
            assertThat(response.data).isEqualTo(123)
        }

        @Test
        fun success_empty_data() {
            val response = ApiResponse.success<Any>(SuccessCode.CREATED, message = "생성 완료")

            assertThat(response.code).isEqualTo(SuccessCode.CREATED.code)
            assertThat(response.message).isEqualTo("생성 완료")
            assertThat(response.data).isNull()
        }
    }

    @Nested
    inner class Error {
        @Test
        fun success_normal() {
            val response = ApiResponse.failure(ErrorCode.INVALID_PARAM, data = "에러 발생")

            assertThat(response.code).isEqualTo(ErrorCode.INVALID_PARAM.code)
            assertThat(response.message).isEqualTo(ErrorCode.INVALID_PARAM.message)
            assertThat(response.data).isEqualTo("에러 발생")
        }

        @Test
        fun success_override_message() {
            val response = ApiResponse.failure(ErrorCode.INVALID_PARAM, message = "크레딧 부족", data = null)

            assertThat(response.code).isEqualTo(ErrorCode.INVALID_PARAM.code)
            assertThat(response.message).isEqualTo("크레딧 부족")
            assertNull(response.data)
        }

        @Test
        fun success_empty_data() {
            val response = ApiResponse.failure<Any>(ErrorCode.INVALID_PARAM, message = "크레딧 부족")

            assertThat(response.code).isEqualTo(ErrorCode.INVALID_PARAM.code)
            assertThat(response.message).isEqualTo("크레딧 부족")
            assertNull(response.data)
        }
    }
}