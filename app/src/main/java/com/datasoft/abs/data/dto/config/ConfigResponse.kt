package com.datasoft.abs.data.dto.config

data class ConfigResponse(
    val addessType : List<CommonModel>,
    val contactType : List<CommonModel>,
    val documentType : List<CommonModel>,
    val genderList : List<CommonModel>,
    val statusEnum : List<CommonModel>,
    val religionList : List<CommonModel>,
    val documentTypeList : List<CommonModel>,
    val occupationList : List<CommonModel>,
    val sourceOfFundList : List<CommonModel>,
    val relationList : List<CommonModel>,
    val divisionList : List<CommonModel>,
    val districtList : List<CommonModel>,
    val cascadeAddressType : List<CommonModel>,
    val nationalityList : List<CommonModel>,
    val fingerKey : List<String>,
    val documentConfigData : List<DocumentConfigData>,
    val customerTypeList : List<CommonModel>,
    val maritalStatusList : List<CommonModel>,
    val salutationList : List<CommonModel>,
    val educationList : List<CommonModel>,
)