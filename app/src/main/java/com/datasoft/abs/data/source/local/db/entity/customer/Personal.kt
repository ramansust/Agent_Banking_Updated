package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "customer_personal_info",
    foreignKeys = [ForeignKey(
        entity = General::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("generalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class Personal(
    @ColumnInfo(name = "marital_status") val maritalStatus: Int?,
    @ColumnInfo(name = "spouse_name") val spouseName: String?,
    val religion: Int?,
    @ColumnInfo(name = "no_of_dependencies") val noOfDependencies: Int?,
    val education: Int?,
    val occupation: Int?,
    val nationality: Int?,
    @ColumnInfo(name = "birth_certificate_no") val birthCertificateNo: String?,
    @ColumnInfo(name = "vat_registration") val vatRegistration: String?,
    @ColumnInfo(name = "driving_license") val drivingLicense: String?,
    @ColumnInfo(name = "monthly_income") val monthIncome: String?,
    @ColumnInfo(name = "source_fund") val sourceFund: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}