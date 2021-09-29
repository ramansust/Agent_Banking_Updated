package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createCustomer.Addresses
import com.datasoft.abs.data.dto.createCustomer.Contact

@Entity(
    tableName = "customer_address_info",
    foreignKeys = [ForeignKey(
        entity = General::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("generalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class Address(
    @ColumnInfo(name = "address_type") val addressType: Int?,
    @ColumnInfo(name = "house_no") val houseNo: String?,
    @ColumnInfo(name = "flat_no") val flatNo: String?,
    val village: String?,
    @ColumnInfo(name = "block_no") val blockNo: String?,
    @ColumnInfo(name = "road_no") val roadNo: String?,
    @ColumnInfo(name = "word_no") val wordNo: String?,
    @ColumnInfo(name = "zip_code") val zipCode: String?,
    @ColumnInfo(name = "post_code") val postCode: String?,
    val state: String?,
    val country: Int?,
    val city: String?,
    val district: Int?,
    val thana: Int?,
    val union: Int?,
    @ColumnInfo(name = "contact_type") val contactType: Int?,
    @ColumnInfo(name = "contact_no") val contactNo: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}

fun Address.toAddress(): Addresses {
    return Addresses(
        "$houseNo, $village",
        addressType!!,
        country!!,
        district!!,
        postCode!!,
        thana!!,
    )
}

fun Address.toContact(): Contact {
    return Contact(
        contactNo!!,
        contactType!!,
        true
    )
}