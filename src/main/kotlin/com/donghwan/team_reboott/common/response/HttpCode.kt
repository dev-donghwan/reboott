package com.donghwan.team_reboott.common.response

import org.springframework.http.HttpStatus

sealed interface HttpCode {
    val status: HttpStatus
    val code: String
    val message: String
}

enum class SuccessCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
) : HttpCode {
    OK(HttpStatus.OK, "SUCCESS_OK", "요청 성공"),
    CREATED(HttpStatus.CREATED, "SUCCESS_CREATED", "생성 성공")
}

enum class ErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
) : HttpCode {
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "INVALID_PARAM", "잘못된 인자"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "")
}