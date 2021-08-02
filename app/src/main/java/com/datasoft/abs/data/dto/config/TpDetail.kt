package com.datasoft.abs.data.dto.config

data class TpDetail(
    val code: Int,
    val limitDailyTrnAmt: Int,
    val limitMaxTrnAmt: Int,
    val limitMonthlyTrnAmt: Int,
    val limitNoOfDailyTrn: Int,
    val limitNoOfMonthlyTrn: Int,
    val transactionProfileName: Int,
    val transactionProfileTypeId: Int,
    var name: String
)