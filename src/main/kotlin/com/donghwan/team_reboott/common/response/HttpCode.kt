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
    OK(HttpStatus.OK, "SUCCESS_OK", "success"),
    CREATED(HttpStatus.CREATED, "SUCCESS_CREATED", "create success")
}

enum class ErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
) : HttpCode {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND", "The requested entity was not found."),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "INVALID_PARAM", "Invalid request parameters."),
    ID_INITIALIZE(HttpStatus.INTERNAL_SERVER_ERROR, "ID_INITIALIZE", "The entity ID has not been initialized."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected server error occurred.")
}