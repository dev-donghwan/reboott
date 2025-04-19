package com.donghwan.team_reboott.common.http

import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.common.response.SuccessCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.collections.Map

class HttpResponseTest {

    @Test
    fun success() {
        val response = HttpResponse.success(code = SuccessCode.OK, data = "hello")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.code).isEqualTo(SuccessCode.OK.code)
        assertThat(response.body?.message).isEqualTo(SuccessCode.OK.message)
        assertThat(response.body?.data).isEqualTo("hello")
    }

    @Test
    fun failure() {
        val response = HttpResponse.failure<List<Any>>(code = ErrorCode.INVALID_PARAM, message = "입력 오류", data = Collections.emptyList())

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.code).isEqualTo(ErrorCode.INVALID_PARAM.code)
        assertThat(response.body?.message).isEqualTo("입력 오류")
        assertThat(response.body?.data).isEmpty()
    }

    @Test
    fun custom() {
        val result = HttpResponse.custom(HttpStatus.ACCEPTED, "custom body")

        assertThat(result.statusCode).isEqualTo(HttpStatus.ACCEPTED)
        assertThat(result.body).isEqualTo("custom body")
    }
}