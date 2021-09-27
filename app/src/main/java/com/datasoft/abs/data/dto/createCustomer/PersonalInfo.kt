package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.Guardian
import com.datasoft.abs.data.source.local.db.entity.customer.Nominee
import com.datasoft.abs.data.source.local.db.entity.customer.Personal

data class PersonalInfo(
    val maritalStatus: Int,
    val spouseName: String,
    val religion: Int,
    val religionValue: String,
    val numberOfDependents: String,
    val education: Int,
    val educationValue: String,
    val occupation: Int,
    val occupationValue: String,
    val nationality: Int,
    val nationalityValue: String,
    val birthCertificateNo: String,
    val vatRegistrationNo: String,
    val drivingLicense: String,
    val monthlyIncome: String,
    val sourceOfFund: String,
    val nomineeName: String,
    val nomineeMobile: String,
    val nomineeAddress: String,
    val nomineeRelation: Int,
    val nomineeEmail: String,
    val guardianName: String,
    val guardianRelation: Int,
    val guardianRelationValue: String,
    val guardianContact: String,
    val guardianAddress: String,
    val guardianDob: String
)

fun PersonalInfo.toPersonal(): Personal {
    return Personal(
        maritalStatus,
        spouseName,
        religion,
        numberOfDependents.toInt(),
        education,
        occupation,
        nationality,
        birthCertificateNo,
        vatRegistrationNo,
        drivingLicense,
        monthlyIncome,
        sourceOfFund
    )
}

fun PersonalInfo.toNominee(): Nominee {
    return Nominee(
        nomineeName,
        nomineeRelation,
        nomineeMobile,
        nomineeEmail,
        nomineeAddress
    )
}

fun PersonalInfo.toGuardian(): Guardian {
    return Guardian(
        guardianName,
        guardianAddress,
        guardianRelation,
        minorDate = "23.02.2021",
        guardianContact,
        guardianDob
    )
}
