package com.datasoft.abs.data.dto.createCustomer

data class GuardianInfo(
    var contactNo: String = "",
    var dob: String = "",
    var docBackSide: String = "",
    var docFrontSide: String = "",
    var documentTypeId: Int = 0,
    var fatherSpouseName: String = "",
    var nameOfGuardian: String = "",
    var permanentAddress: String = "",
    var presentAddress: String = "",
    var profilePhoto: String = "",
    var relationShipId: Int = 0,
    var signaturePhoto: String = ""
)