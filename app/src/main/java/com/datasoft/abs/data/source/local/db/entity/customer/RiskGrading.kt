package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "customer_risk_grading",
    foreignKeys = [ForeignKey(
        entity = General::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("generalId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
class RiskGrading(
    @ColumnInfo(name = "boarding_type") val boardingType: Int?,
    @ColumnInfo(name = "resident_status") val residentStatus: Int?,
    @ColumnInfo(name = "black_listed") val blackListed: Int?,
    @ColumnInfo(name = "politically_exposed_person") val politicallyExposedPerson: Int?,
    @ColumnInfo(name = "close_associates_related") val closeAssociatesRelated: Int?,
    @ColumnInfo(name = "interviewed_personally") val interviewedPersonally: Int?,
    @ColumnInfo(name = "product_type") val productType: Int?,
    val profession: Int?,
    @ColumnInfo(name = "yearly_transaction_worth") val yearlyTransactionWorth: Int?,
    @ColumnInfo(name = "transparency_risk") val transparencyRisk: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}