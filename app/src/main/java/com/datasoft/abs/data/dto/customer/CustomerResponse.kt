package com.datasoft.abs.data.dto.customer

data class CustomerResponse(
    val firstItemOnPage: Int,
    val rows: List<Row>,
    val total: Int
)