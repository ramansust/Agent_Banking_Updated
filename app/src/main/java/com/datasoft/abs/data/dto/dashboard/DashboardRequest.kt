package com.datasoft.abs.data.dto.dashboard

data class DashboardRequest(
    val BranchId: Int,
    val FromDate: String,
    val ToDate: String
)