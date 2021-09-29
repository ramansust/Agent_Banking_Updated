package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_nominee_guardian_info",
    foreignKeys = [ForeignKey(
        entity = AccountNominee::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("nomineeId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class NomineeGuardian(
    val name: String?,
    @ColumnInfo(name = "father_spouse_name") val fatherSpouseName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    @ColumnInfo(name = "present_address") val presentAddress: String?,
    @ColumnInfo(name = "permanent_address") val permanentAddress: String?,
    val relation: Int?,
    @ColumnInfo(name = "document_type") val documentType: Int?,
    @ColumnInfo(name = "id_value") val idValue: String?,
    @ColumnInfo(name = "expiry_date") val expiryDate: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nomineeId: Int = 0
}