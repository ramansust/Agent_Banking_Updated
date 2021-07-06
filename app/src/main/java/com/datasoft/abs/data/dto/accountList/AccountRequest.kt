package com.datasoft.abs.data.dto.accountList

import com.datasoft.abs.presenter.utils.Constant.PER_PAGE_ITEM

data class AccountRequest(
    val pageNumber: Int,
    val pageSize: Int = PER_PAGE_ITEM,
    val status: String
)