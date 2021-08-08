package com.datasoft.abs.data.dto.createAccount.review

import java.io.Serializable

data class Nominee(
    val name: String,
    val fatherName: String,
    val motherName: String,
    val dob: String,
    val spouseName: String,
    val shareOfPercentage: Int,
    val relationship: Int,
    val occupationId: Int,
    val docTypeId: Int,
    val nidNo: String,
    val expiryDate: String,
    val permanentAddress: String,
    val presentAddress: String,
    val applicant: String,
    var photo: String,
    var signaturePhoto: String,
    var nidFrontPhoto: String,
    var nidBackPhoto: String,
    val remainMinor: NomineeRemainMinor?
) : Serializable