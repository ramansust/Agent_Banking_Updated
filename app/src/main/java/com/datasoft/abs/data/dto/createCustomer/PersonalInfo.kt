package com.datasoft.abs.data.dto.createCustomer

data class PersonalInfo(
    val maritalStatus: Int,
    val spouseName: String,
    val religion: Int,
    val numberOfDependents: String,
    val education: Int,
    val occupation: Int,
    val nationality: Int,
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
    val guardianContact: String,
    val guardianAddress: String,
    val guardianDob: String
)
