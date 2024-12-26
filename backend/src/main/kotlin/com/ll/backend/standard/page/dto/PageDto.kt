package com.ll.backend.standard.page.dto

import org.springframework.data.domain.Page

data class PageDto<T>(
    val items: List<T>,
    val totalItems: Long,
    val totalPages: Int,
    val currentPageNumber: Int,
    val pageSize: Int
) {
    constructor(page: Page<T>) : this(
        items = page.content,
        totalItems = page.totalElements,
        totalPages = page.totalPages,
        currentPageNumber = page.number + 1,
        pageSize = page.size
    )
}