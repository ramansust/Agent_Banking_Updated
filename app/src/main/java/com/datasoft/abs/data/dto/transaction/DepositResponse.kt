package com.datasoft.abs.data.dto.transaction

data class DepositResponse(
    val firstItemOnPage: Int?,
    val rows: List<Row>?,
    val total: Int?
)