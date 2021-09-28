package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createAccount.general.AccountInfo

@Entity(
    tableName = "account_account_info"
)
data class Account(
    @ColumnInfo(name = "product_category") val productCategory: Int?,
    @ColumnInfo(name = "account_type") val accountType: Int?,
    @ColumnInfo(name = "operating_instruction") val operatingInstruction: Int?,
    @ColumnInfo(name = "customer_name") val customerName: String?,
    @ColumnInfo(name = "account_title") val accountTitle: String?,
    @ColumnInfo(name = "opening_date") val openingDate: String?,
    val currency: Int?,
    @ColumnInfo(name = "source_fund") val sourceFund: Int?,
    @ColumnInfo(name = "initial_amount") val initialAmount: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun Account.toAccountInfo(): AccountInfo {
    return AccountInfo(
        productCategory!!,
        accountType!!,
        operatingInstruction!!,
        customerName!!,
        accountTitle!!,
        openingDate!!,
        currency!!,
        sourceFund!!,
        initialAmount!!
    )
}