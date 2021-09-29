package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.datasoft.abs.data.dto.createCustomer.PhotoInfo

@Entity(
    tableName = "customer_photo_info",
    foreignKeys = [ForeignKey(
        entity = General::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("generalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class Photo(
    @ColumnInfo(name = "user_profile") val userProfile: String?,
    @ColumnInfo(name = "user_signature") val userSignature: String?,
    @ColumnInfo(name = "user_nid_front") val userNidFront: String?,
    @ColumnInfo(name = "user_nid_back") val userNidBack: String?,
    @ColumnInfo(name = "guardian_profile") val guardianProfile: String?,
    @ColumnInfo(name = "guardian_signature") val guardianSignature: String?,
    @ColumnInfo(name = "guardian_nid_front") val guardianNidFront: String?,
    @ColumnInfo(name = "guardian_nid_back") val guardianNidBack: String?,
    @ColumnInfo(name = "document_type") val documentType: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}

fun Photo.toPhotoInfo(): PhotoInfo {
    return PhotoInfo(
        id,
        userProfile,
        userSignature,
        userNidFront,
        userNidBack,
        guardianProfile,
        guardianSignature,
        guardianNidFront,
        guardianNidBack,
        documentType
    )
}