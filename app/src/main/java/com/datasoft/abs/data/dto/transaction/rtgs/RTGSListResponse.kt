package com.datasoft.abs.data.dto.transaction.rtgs

data class RTGSListResponse(
    var rows: List<Row>,
    var pageNumber: Int
)