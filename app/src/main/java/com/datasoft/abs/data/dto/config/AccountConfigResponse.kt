package com.datasoft.abs.data.dto.config

data class AccountConfigResponse(
    val currencyList: List<CommonModel>,
    val customerList: List<CommonModel>,
    val documentTypeList: List<CommonModel>,
    val operationInstructionList: List<CommonModel>,
    val productCategoryList: List<CommonModel>,
    val productConfig: List<ProductConfig>,
    val relatedDocTypeList: List<CommonModel>,
    val relationList: List<CommonModel>,
    val sourceOfFundList: List<CommonModel>,
    val occupationList: List<CommonModel>
)