package com.datasoft.abs.data.dto.customerList

data class CustomerResponse(
    val firstItemOnPage: Int,
    var rows: List<Row>,
    val total: Int,
    var pageNumber: Int
)