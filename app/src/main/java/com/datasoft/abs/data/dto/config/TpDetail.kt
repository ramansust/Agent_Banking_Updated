package com.datasoft.abs.data.dto.config

data class TpDetail(
    val Code: Int,
    val LimitDailyTrnAmt: Int,
    val LimitMaxTrnAmt: Int,
    val LimitMonthlyTrnAmt: Int,
    val LimitNoOfDailyTrn: Int,
    val LimitNoOfMonthlyTrn: Int,
    val TransactionProfileName: Int,
    val TransactionProfileTypeId: Int
)