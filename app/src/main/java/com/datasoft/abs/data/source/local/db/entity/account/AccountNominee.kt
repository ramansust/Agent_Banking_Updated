package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_nominee_info")
data class AccountNominee(
    @ColumnInfo(name = "full_name")val fullName: String?,
    @ColumnInfo(name = "father_name") val fatherName: String?,
    @ColumnInfo(name = "mother_name") val motherName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    @ColumnInfo(name = "spouse_name") val spouseName: String?,
    @ColumnInfo(name = "share_percent") val sharePercent: Int?,
    val relation: Int?,
    val occupation: Int?,
    @ColumnInfo(name = "document_type") val documentType: Int?,
    @ColumnInfo(name = "id_value") val idValue: Int?,
    @ColumnInfo(name = "expiry_date") val expiryDate: Int?,
    @ColumnInfo(name = "permanent_address") val permanentAddress: Int?,
    @ColumnInfo(name = "present_address") val presentAddress: Int?,
    val applicant: Int?,
    val photo: String?,
    val signature: String?,
    @ColumnInfo(name = "front_side") val frontSide: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}