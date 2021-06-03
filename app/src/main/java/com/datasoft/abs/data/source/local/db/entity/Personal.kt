package com.datasoft.abs.data.source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_info")
class Personal(
    @ColumnInfo(name = "salutation") val salutation: String?,
    @ColumnInfo(name = "dob") val dob: String?,
    @ColumnInfo(name = "father_name") val fatherName: String?,
    @ColumnInfo(name = "marital_status") val maritalStatus: String?,
    @ColumnInfo(name = "religion") val religion: String?,
    @ColumnInfo(name = "education") val education: String?,
    @ColumnInfo(name = "nationality") val nationality: String?,
    @ColumnInfo(name = "vat_registration") val vatRegistration: String?,
    @ColumnInfo(name = "residential_status") val residentialStatus: String?,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "mother_name") val motherName: String?,
    @ColumnInfo(name = "spouse_name") val spouseName: String?,
    @ColumnInfo(name = "no_of_dependencies") val noOfDependencies: String?,
    @ColumnInfo(name = "occupation") val occupation: String?,
    @ColumnInfo(name = "birth_certificate_no") val birthCertificateNo: String?,
    @ColumnInfo(name = "driving_license") val drivingLicense: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}