package com.datasoft.abs.data.source.local.db.entity.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createAccount.general.CustomerData

@Entity(
    tableName = "account_customer_info",
    foreignKeys = [ForeignKey(
        entity = Account::class, parentColumns = arrayOf("id"),
        childColumns = arrayOf("id"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Customer(
    val name: String?,
    @ColumnInfo(name = "father_name") val fatherName: String?,
    @ColumnInfo(name = "mother_name") val motherName: String?,
    @ColumnInfo(name = "birth_date") val birthDate: String?,
    val signature: Boolean?,
    val mandatory: Boolean?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var accountId: Int = 0
}

fun Customer.toCustomerData(): CustomerData {
    return CustomerData(
        customerId = 1,
        customerNo = "1",
        dob = birthDate!!,
        fatherName = fatherName!!,
        fullName = name!!,
        gender = "1",
        isRequired = mandatory!!,
        isSignatory = signature!!,
        motherName = motherName!!
    )
}