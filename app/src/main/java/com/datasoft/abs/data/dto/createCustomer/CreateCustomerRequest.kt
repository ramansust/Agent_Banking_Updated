package com.datasoft.abs.data.dto.createCustomer

data class CreateCustomerRequest(
    val customerNo: String,
    val email: String,
    val fingerPrint: String,
    val gender: Int,
    val message: String,
    val mobile: String,
    val nationalityId: Int,
    val nidBackSide: String,
    val nidFrontSide: String,
    val nidNo: String,
    val occupationId: Int,
    val permanentAddress: PermanentAddress,
    val presentAddress: PresentAddress,
    val profilePhoto: String,
    val relatedDocs: List<RelatedDoc>,
    val religion: Int,
    val signaturePhoto: String,
    val spouseName: String,
    val status: Boolean
)