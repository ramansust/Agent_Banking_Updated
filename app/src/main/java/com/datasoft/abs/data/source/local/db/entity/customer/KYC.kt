package com.datasoft.abs.data.source.local.db.entity.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_kyc_info")
class KYC(
    @ColumnInfo(name = "boarding_type") val boardingType: Int?,
    @ColumnInfo(name = "resident_status") val residentStatus: Int?,
    @ColumnInfo(name = "black_listed") val blackListed: Int?,
    @ColumnInfo(name = "politically_exposed_person") val politicallyExposedPerson: Int?,
    @ColumnInfo(name = "close_associates_related") val closeAssociatesRelated: Int?,
    @ColumnInfo(name = "interviewed_personally") val interviewedPersonally: Int?,
    @ColumnInfo(name = "product_type") val productType: Int?,
    val profession: Int?,
    @ColumnInfo(name = "yearly_transaction_worth") val yearlyTransactionWorth: Int?,
    @ColumnInfo(name = "transparency_risk") val transparencyRisk: Int?,

    @ColumnInfo(name = "nid_collected") val nidCollected: Boolean?,
    @ColumnInfo(name = "nid_verified") val nidVerified: Boolean?,
    @ColumnInfo(name = "passport_collected") val passportCollected: Boolean?,
    @ColumnInfo(name = "password_verified") val passportVerified: Boolean?,
    @ColumnInfo(name = "birth_certificate_collected") val birthCertificateCollected: Boolean?,
    @ColumnInfo(name = "birth_certificate_verified") val birthCertificateVerified: Boolean?,
    @ColumnInfo(name = "tin_collected") val tinCollected: Boolean?,
    @ColumnInfo(name = "tin_verified") val tinVerified: Boolean?,
    @ColumnInfo(name = "driving_license_collected") val drivingLicenseCollected: Boolean?,
    @ColumnInfo(name = "driving_license_verified") val drivingLicenseVerified: Boolean?,
    @ColumnInfo(name = "vat_registration_collected") val vatRegistrationCollected: Boolean?,
    @ColumnInfo(name = "vat_registration_verified") val vatRegistrationVerified: Boolean?,
    @ColumnInfo(name = "organization_registration_collected") val organizationRegistrationCollected: Boolean?,
    @ColumnInfo(name = "organization_registration_verified") val organizationRegistrationVerified: Boolean?,
    @ColumnInfo(name = "certificate_incorporation_collected") val certificateIncorporationCollected: Boolean?,
    @ColumnInfo(name = "certificate_incorporation_verified") val certificateIncorporationVerified: Boolean?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var generalId: Int = 0
}