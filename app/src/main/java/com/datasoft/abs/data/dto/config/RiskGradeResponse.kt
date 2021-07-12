package com.datasoft.abs.data.dto.config

data class RiskGradeResponse(
    val typeOfOnboarding : List<CommonModel>,
    val residentStatus : List<CommonModel>,
    val blackListed : List<CommonModel>,
    val isPep : List<CommonModel>,
    val isPepCloser : List<CommonModel>,
    val isInterviewedPersonally : List<CommonModel>,
    val typeOfProduct : List<CommonModel>,
    val business : List<CommonModel>,
    val profession : List<CommonModel>,
    val transactionalRisk : List<CommonModel>,
    val transparencyRisk : List<CommonModel>,
)