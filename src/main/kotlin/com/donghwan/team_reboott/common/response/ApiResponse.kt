package com.donghwan.team_reboott.common.response

data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(code: SuccessCode, message: String? = null, data: T? = null): ApiResponse<T> {
            return ApiResponse(code.code, message ?: code.message, data)
        }

        fun <T> failure(code: ErrorCode, message: String? = null, data: T? = null): ApiResponse<T> {
            return ApiResponse(code.code, message ?: code.message, data)
        }
    }
}