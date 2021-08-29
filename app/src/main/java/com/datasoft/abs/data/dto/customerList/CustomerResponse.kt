package com.datasoft.abs.data.dto.customerList

data class CustomerResponse(
    val firstItemOnPage: Int,
    val rows: List<Row>,
    val total: Int,
    var pageNumber: Int
)