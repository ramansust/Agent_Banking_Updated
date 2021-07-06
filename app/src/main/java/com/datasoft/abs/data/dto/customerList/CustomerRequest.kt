package com.datasoft.abs.data.dto.customerList

import com.datasoft.abs.presenter.utils.Constant.PER_PAGE_ITEM

data class CustomerRequest(
    val pageNumber: Int,
    val pageSize: Int = PER_PAGE_ITEM,
    val status: String
)