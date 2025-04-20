package com.donghwan.team_reboott.common.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

data class PageRequest(
    private val page: Int = DEFAULT_PAGE,
    private val size: Int = DEFAULT_PAGE_SIZE
) {
    fun getOriginalPageIndex(): Int = page

    fun getPageIndex(): Int = if (page > 0) page - 1 else 0

    fun getPageSize(): Int = if (size > 0) size else DEFAULT_PAGE_SIZE

    fun toPageable(): Pageable = PageRequest.of(getPageIndex(), getPageSize())

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 20
    }
}