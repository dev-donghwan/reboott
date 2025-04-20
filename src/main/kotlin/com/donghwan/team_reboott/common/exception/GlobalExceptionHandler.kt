package com.donghwan.team_reboott.common.exception

import com.donghwan.team_reboott.common.alias.Result
import com.donghwan.team_reboott.common.extension.logger
import com.donghwan.team_reboott.common.http.HttpResponse
import com.donghwan.team_reboott.common.response.ErrorCode
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(ex: GlobalException): Result<Unit> {
        ex.cause?.let {
            logger().error("GlobalException occurred: ${ex.message}", it)
        } ?: logger().error("GlobalException occurred: ${ex.message}")

        return HttpResponse.failure(
            code = ex.errorCode,
            message = ex.message
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): Result<Unit> {
        logger().error("Unhandled RuntimeException: ${ex.message}", ex)
        return HttpResponse.failure(
            code = ErrorCode.INTERNAL_SERVER_ERROR,
            message = "서버에서 오류가 발생했습니다.",
            data = null
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): Result<Unit> {
        logger().error("Unhandled Exception: ${ex.message}", ex)
        return HttpResponse.failure(
            code = ErrorCode.INTERNAL_SERVER_ERROR,
            message = "예상치 못한 오류가 발생했습니다.",
            data = null
        )
    }
}