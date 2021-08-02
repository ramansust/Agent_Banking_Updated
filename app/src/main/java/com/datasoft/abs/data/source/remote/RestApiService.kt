package com.datasoft.abs.data.source.remote

import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.accountList.AccountResponse
import com.datasoft.abs.data.dto.config.*
import com.datasoft.abs.data.dto.createAccount.general.CustomerDataResponse
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerRequest
import com.datasoft.abs.data.dto.createCustomer.CreateCustomerResponse
import com.datasoft.abs.data.dto.customerList.CustomerRequest
import com.datasoft.abs.data.dto.customerList.CustomerResponse
import com.datasoft.abs.data.dto.dashboard.DashboardRequest
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.login.LoginRequest
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface RestApiService {
    @POST("api/login")
    suspend fun performLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/teller-dashboard")
    suspend fun getDashboardData(@Body dashboardRequest: DashboardRequest): Response<DashboardResponse>

    @GET("api/customer-general-config")
    suspend fun getConfigData() : Response<ConfigResponse>

    @POST("api/customer-list")
    suspend fun getCustomerListData(@Body customerRequest: CustomerRequest): Response<CustomerResponse>

    @POST("api/account-list")
    suspend fun getAccountListData(@Body accountRequest: AccountRequest): Response<AccountResponse>

    @POST("api/sanction-screening")
    suspend fun getSanctionScreeningData(@Body sanctionScreeningRequest: SanctionScreeningRequest): Response<SanctionScreeningResponse>

    @POST("api/dedupe-check")
    suspend fun getDedupeCheckData(@Body dedupeCheckRequest: DedupeCheckRequest): Response<DedupeCheckResponse>

    @POST("api/create-customer")
    suspend fun createCustomerData(@Body createCustomerRequest: CreateCustomerRequest): Response<CreateCustomerResponse>

    @POST("api/deposit-list")
    suspend fun getDepositData(@Body commonRequest: CommonRequest): Response<Objects>

    @POST("api/withdraw-list")
    suspend fun getWithdrawData(@Body commonRequest: CommonRequest): Response<Objects>

    @POST("api/transfer-list")
    suspend fun getTransferData(@Body commonRequest: CommonRequest): Response<Objects>

    @GET("api/cascade-address")
    suspend fun getCascadeAddress(@Query("forArea") area: Int, @Query("id") id: Int) : Response<List<CommonModel>>

    @GET("api/riskgrade-config")
    suspend fun getRiskGradeConfig() : Response<RiskGradeResponse>

    @GET("api/account-general-config")
    suspend fun getAccountConfig() : Response<AccountConfigResponse>

    @GET("api/tp-config")
    suspend fun getTransactionProfileConfig() : Response<List<TransactionProfileConfig>>

    @GET("api/customer-details-list")
    suspend fun getCustomerData(@Query("custId") customerID: String) : Response<CustomerDataResponse>
}