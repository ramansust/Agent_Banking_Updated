package com.datasoft.abs.data.dto.config

data class TpDetail(
    val code: Int,
    var limitDailyTrnAmt: Int,
    var limitMaxTrnAmt: Int,
    var limitMonthlyTrnAmt: Int,
    var limitNoOfDailyTrn: Int,
    var limitNoOfMonthlyTrn: Int,
    val transactionProfileName: Int,
    val transactionProfileTypeId: Int,
    var profileName: String,
    var codeName: String
)