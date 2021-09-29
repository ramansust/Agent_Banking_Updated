package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_other_facilities",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("accountId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class OtherFacilities(
    val name: String?,
    val isChecked: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var accountId: Int = 0
}