package com.datasoft.abs.data.dto.dashboard

data class DashboardResponse(
    val accountList: List<Account>,
    val fromDate: String,
    val productWise: List<ProductWise>,
    val toDate: String,
    val totalApplied: Int,
    val totalApproved: Int,
    val totalOnHold: Int,
    val totalPending: Int
)