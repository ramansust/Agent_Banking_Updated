package com.datasoft.abs.data.dto.createCustomer

import com.datasoft.abs.data.source.local.db.entity.customer.RiskGrading

data class KYCInfo(
    val typeOfOnboarding: Int,
    val residentStatus: Int,
    val blackListed: Int,
    val isPep: Int,
    val isPepCloser: Int,
    val isInterviewedPersonally: Int,
    val typeOfProduct: Int,
    val profession: Int,
    val transactionalRisk: Int,
    val transparencyRisk: Int
)

fun KYCInfo.toRiskGrading(): RiskGrading {
    return RiskGrading(
        typeOfOnboarding,
        residentStatus,
        blackListed,
        isPep,
        isPepCloser,
        isInterviewedPersonally,
        typeOfProduct,
        profession,
        transactionalRisk,
        transparencyRisk
    )
}
