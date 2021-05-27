package com.datasoft.abs.data.dto.config

data class ConfigResponse(
    val documentTypeList: List<DocumentType>,
    val genderList: List<Gender>,
    val message: String,
    val occupationList: List<Occupation>,
    val relationList: List<Relation>,
    val religionList: List<Religion>,
    val sourceOfFundList: List<SourceOfFund>,
    val statusCode: Int
)