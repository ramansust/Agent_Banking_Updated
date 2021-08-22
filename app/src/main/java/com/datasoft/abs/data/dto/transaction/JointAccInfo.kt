package com.datasoft.abs.data.dto.transaction

data class JointAccInfo(
    val applicantNumber: Int?,
    val customerId: Int?,
    val fingerData: String?,
    val isRequired: Boolean?,
    val isSignatory: Boolean?
)