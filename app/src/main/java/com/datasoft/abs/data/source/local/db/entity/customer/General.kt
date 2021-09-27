package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_general_info")
data class General(
    val salutation: Int?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "date_birth") val dateBirth: String?,
    val nid: String?,
    val gender: Int?,
    @ColumnInfo(name = "customer_type") val customerType: Int?,
    @ColumnInfo(name = "mobile_number") val mobileNumber: String?,
    @ColumnInfo(name = "mother_name") val motherName: String?,
    @ColumnInfo(name = "father_name") val fatherName: String?,
    val city: String?,
    val country: Int?,
    @ColumnInfo(name = "branch_id") val branchId: Int?,
    @ColumnInfo(name = "customer_no") val customerNo: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}