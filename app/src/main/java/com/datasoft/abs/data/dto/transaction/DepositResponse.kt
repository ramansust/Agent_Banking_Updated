package com.datasoft.abs.data.dto.transaction

data class DepositResponse(
    val firstItemOnPage: Int,
    var rows: List<Row>?,
    val total: Int,
    var pageNumber: Int
)