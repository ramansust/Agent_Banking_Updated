package com.datasoft.abs.domain

import androidx.lifecycle.LiveData
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.accountList.AccountResponse
import com.datasoft.abs.data.dto.config.ConfigResponse
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerRequest
import com.datasoft.abs.data.dto.customerList.CustomerRequest
import com.datasoft.abs.data.dto.customerList.CustomerResponse
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import retrofit2.Response
import java.util.*

interface Repository {
    suspend fun performLogin(userName: String, password: String): Response<LoginResponse>
    fun setAuthToken(token: String)
    suspend fun getDashboardData(dayNo: Int): Response<DashboardResponse>

    suspend fun getConfigData(): Response<ConfigResponse>
    suspend fun getCustomerListData(customerRequest: CustomerRequest): Response<CustomerResponse>
    suspend fun getAccountListData(accountRequest: AccountRequest): Response<AccountResponse>
    suspend fun getSanctionScreeningData(sanctionScreeningRequest: SanctionScreeningRequest): Response<SanctionScreeningResponse>
    suspend fun getDedupeCheckData(dedupeCheckRequest: DedupeCheckRequest): Response<DedupeCheckResponse>

    suspend fun createCustomerData(createCustomerRequest: CreateCustomerRequest): Response<Objects>
    suspend fun getDepositData(commonRequest: CommonRequest): Response<Objects>
    suspend fun getWithdrawData(commonRequest: CommonRequest): Response<Objects>
    suspend fun getTransferData(commonRequest: CommonRequest): Response<Objects>

    suspend fun insert(generalInfo: GeneralInfo)
    fun getAll(): LiveData<List<GeneralInfo>>
}