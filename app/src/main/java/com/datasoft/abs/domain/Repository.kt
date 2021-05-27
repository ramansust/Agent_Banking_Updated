package com.datasoft.abs.domain

import androidx.lifecycle.LiveData
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import retrofit2.Response

interface Repository {
    suspend fun performLogin(userName: String, password: String): Response<LoginResponse>
    fun setAuthToken(token: String)
    suspend fun getDashboardData(
        branchId: Int,
        fromDate: String,
        toDate: String
    ): Response<DashboardResponse>

    suspend fun insert(generalInfo: GeneralInfo)
    fun getAll(): LiveData<List<GeneralInfo>>
}