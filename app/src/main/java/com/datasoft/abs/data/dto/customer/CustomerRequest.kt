package com.datasoft.abs.data.dto.customer

import com.datasoft.abs.presenter.utils.Constant.PER_PAGE_ITEM

data class CustomerRequest(
    val pageNumber: Int,
    val pageSize: Int = PER_PAGE_ITEM,
    val status: String
)