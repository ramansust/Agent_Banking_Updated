package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_transaction_profile_info")
class TransactionProfile(
    @ColumnInfo(name = "transaction_type") val transactionType: String?,
    @ColumnInfo(name = "daily_transaction") val dailyTransaction: Int?,
    @ColumnInfo(name = "daily_amount") val dailyAmount: Int?,
    @ColumnInfo(name = "monthly_transaction") val monthlyTransaction: Int?,
    @ColumnInfo(name = "monthly_amount") val monthlyAmount: Int?,
    @ColumnInfo(name = "max_transaction_amount") val maxTransactionAmount: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var accountId: Int = 0
}