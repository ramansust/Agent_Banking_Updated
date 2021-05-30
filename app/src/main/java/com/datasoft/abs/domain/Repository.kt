package com.datasoft.abs.domain

import androidx.lifecycle.LiveData
import com.datasoft.abs.data.dto.config.ConfigResponse
import com.datasoft.abs.data.dto.customer.CustomerRequest
import com.datasoft.abs.data.dto.customer.CustomerResponse
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import retrofit2.Response

interface Repository {
    suspend fun performLogin(userName: String, password: String): Response<LoginResponse>
    fun setAuthToken(token: String)
    suspend fun getDashboardData(dayNo: Int): Response<DashboardResponse>

    suspend fun getConfigData(): Response<ConfigResponse>
    suspend fun getCustomerListData(customerRequest: CustomerRequest): Response<CustomerResponse>
    suspend fun getSanctionScreeningData(sanctionScreeningRequest: SanctionScreeningRequest): Response<SanctionScreeningResponse>
    suspend fun getDedupeCheckData(dedupeCheckRequest: DedupeCheckRequest): Response<DedupeCheckResponse>

    suspend fun insert(generalInfo: GeneralInfo)
    fun getAll(): LiveData<List<GeneralInfo>>
}