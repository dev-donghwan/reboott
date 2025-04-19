package com.donghwan.team_reboott.common.exception

import com.donghwan.team_reboott.common.response.ErrorCode

class GlobalException(
    private val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    fun getErrorCode(): ErrorCode = errorCode
}