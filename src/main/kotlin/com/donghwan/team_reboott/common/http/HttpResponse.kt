package com.donghwan.team_reboott.common.http

import com.donghwan.team_reboott.common.response.ApiResponse
import com.donghwan.team_reboott.common.response.ErrorCode
import com.donghwan.team_reboott.common.response.SuccessCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object HttpResponse {

    fun <T> success(
        code: SuccessCode = SuccessCode.OK,
        message: String? = null,
        data: T? = null
    ): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(code.status).body(
            ApiResponse.success(code, message, data)
        )
    }

    fun <T> failure(
        code: ErrorCode,
        message: String? = null,
        data: T? = null
    ): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(code.status).body(
            ApiResponse.failure(code, message, data)
        )
    }

    fun <T> custom(status: HttpStatus, body: T): ResponseEntity<T> {
        return ResponseEntity.status(status).body(body)
    }
}  