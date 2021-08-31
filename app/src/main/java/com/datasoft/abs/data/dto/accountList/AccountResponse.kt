package com.datasoft.abs.data.dto.accountList

data class AccountResponse(
    val firstItemOnPage: Int,
    var rows: List<Row>,
    val total: Int,
    var pageNumber: Int
)