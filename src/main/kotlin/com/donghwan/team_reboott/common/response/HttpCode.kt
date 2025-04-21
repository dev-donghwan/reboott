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
    INPUT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "INPUT_LIMIT_EXCEEDED", "The usage limit for this feature has been exceeded."),
    FREQUENCY_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "FREQUENCY_LIMIT_EXCEEDED", "The allowed usage frequency for this feature has been exceeded."),
    NOT_IMPLEMENTED(HttpStatus.INTERNAL_SERVER_ERROR, "NOT_IMPLEMENTED", "No matching implementation found for the requested condition."),
    FEATURE_NOT_AVAILABLE(HttpStatus.FORBIDDEN, "FEATURE_NOT_AVAILABLE", "This company does not have permission to use the requested feature."),
    ENTITY_NOT_ASSIGNED(HttpStatus.NOT_FOUND, "ENTITY_NOT_ASSIGNED", "Entity is not assigned."),
    NOT_ENOUGH_CREDIT(HttpStatus.CONFLICT, "NOT_ENOUGH_CREDIT", "Insufficient credit to perform this operation."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND", "The requested entity was not found."),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "INVALID_PARAM", "Invalid request parameters."),
    ID_INITIALIZE(HttpStatus.INTERNAL_SERVER_ERROR, "ID_INITIALIZE", "The entity ID has not been initialized."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "An unexpected server error occurred.")
}