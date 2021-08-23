package com.datasoft.abs.data.dto.transaction

import com.datasoft.abs.data.dto.config.CommonModel


data class AccountDetailsResponse(
    val acType: String?,
    val accountId: Int?,
    val accountNo: String?,
    val accountTitle: String?,
    val balance: Double?,
    val branchId: Int?,
    val configSignApplicable: Boolean?,
    val currencyData: List<CommonModel>?,
    val currencyId: Int?,
    val customerId: Int?,
    val fullName: String?,
    val isEntityAccount: Boolean?,
    val jointAccInfo: List<JointAccInfo>?,
    val nid: String?,
    val numberOfVerifier: Int?,
    val profileImage: String?,
    val serviceAgent: Int?,
    val signatureImage: String?,
    val transaction: List<CommonModel>?,
    val trnProfileType: List<CommonModel>?,
    val trnType: List<CommonModel>?
)