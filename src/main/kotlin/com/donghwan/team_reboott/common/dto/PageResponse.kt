package com.donghwan.team_reboott.common.dto

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElement: Long,
    val pageIndex: Int,
    val pageSize: Int,
    val pageNumberOfElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean
) {

}

fun <T> Page<T>.toPageResponse(): PageResponse<T> {
    return PageResponse(
        content = content,
        totalPages = totalPages,
        totalElement = totalElements,
        pageIndex = number + 1,
        pageSize = size,
        pageNumberOfElements = numberOfElements,
        isFirst = isFirst,
        isLast = isLast,
        hasPrevPage = hasPrevious(),
        hasNextPage = hasNext()
    )
}