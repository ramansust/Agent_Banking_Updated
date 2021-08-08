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
    val expireDate: String,
    val permanentAddress: String,
    val presentAddress: String,
    val applicant: String,
    var photo: String,
    var signaturePhoto: String,
    var nidFrontPhoto: String,
    var nidBackPhoto: String,
    val nomineeMinorInfo: NomineeRemainMinor?
) : Serializable {
    override fun toString(): String {
        return "Nominee(name='$name', fatherName='$fatherName', motherName='$motherName', dob='$dob', spouseName='$spouseName', shareOfPercentage=$shareOfPercentage, relationship=$relationship, occupationId=$occupationId, docTypeId=$docTypeId, nidNo='$nidNo', expireDate='$expireDate', permanentAddress='$permanentAddress', presentAddress='$presentAddress', applicant='$applicant', nomineeMinorInfo=$nomineeMinorInfo)"
    }
}