package com.datasoft.abs.data.dto.createCustomer

data class KYCInfo(
    val typeOfOnboarding : Int,
    val residentStatus : Int,
    val blackListed : Int,
    val isPep : Int,
    val isPepCloser : Int,
    val isInterviewedPersonally : Int,
    val typeOfProduct : Int,
    val profession : Int,
    val transactionalRisk : Int,
    val transparencyRisk : Int
)
