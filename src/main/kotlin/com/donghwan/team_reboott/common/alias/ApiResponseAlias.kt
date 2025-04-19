package com.donghwan.team_reboott.common.alias

import com.donghwan.team_reboott.common.response.ApiResponse
import org.springframework.http.ResponseEntity

typealias Result<T> = ResponseEntity<ApiResponse<T>>