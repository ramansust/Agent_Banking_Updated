package com.datasoft.abs.data.dto.accountList

data class AccountResponse(
    val firstItemOnPage: Int,
    val rows: List<Row>,
    val total: Int
)