package com.datasoft.abs.domain

import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.login.LoginResponse
import retrofit2.Response

interface Repository {
    suspend fun performLogin(userName: String, password: String): Response<LoginResponse>
    fun setAuthToken(token: String)
    suspend fun getDashboardData(
        branchId: Int,
        fromDate: String,
        toDate: String
    ): Response<DashboardResponse>
}