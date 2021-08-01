package com.datasoft.abs.data.dto.config

data class TransactionProfileConfig(
    val tpDetails: List<TpDetail>,
    val transactionCodeList: List<CommonModel>,
    val transactionProfileList: List<CommonModel>
)