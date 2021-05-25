package com.datasoft.abs.data.dto.dashboard

data class ProductWise(
    val approved: Int,
    val date: String,
    val onHold: Int,
    val pending: Int,
    val productName: String
)