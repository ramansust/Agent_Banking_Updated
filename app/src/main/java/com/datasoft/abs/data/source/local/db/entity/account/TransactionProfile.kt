package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_transaction_profile_info",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("accountId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
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