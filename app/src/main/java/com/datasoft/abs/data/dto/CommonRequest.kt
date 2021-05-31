package com.datasoft.abs.data.dto

import com.datasoft.abs.presenter.utils.Constant.PER_PAGE_ITEM

data class CommonRequest(
    val pageNumber: Int,
    val pageSize: Int = PER_PAGE_ITEM
)