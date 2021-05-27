package com.datasoft.abs.data.dto.dashboard

data class VmProductWiseInfo(
    val april: Int,
    val february: Int,
    val january: Int,
    val march: Int,
    val productName: String,
    val productWiseBalanceInHand: Double,
    val productWiseTotalCustomer: Int
)