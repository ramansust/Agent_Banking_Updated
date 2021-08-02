package com.datasoft.abs.data

import androidx.lifecycle.LiveData
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
import com.datasoft.abs.data.source.local.db.dao.GeneralInfoDao
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import com.datasoft.abs.data.source.remote.JwtHelper
import com.datasoft.abs.data.source.remote.RestRemoteDataSource
import com.datasoft.abs.domain.Repository
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    remoteDataSource: RestRemoteDataSource,
    private val jwtHelper: JwtHelper,
    private val generalInfoDao: GeneralInfoDao
) : Repository {
    private val restApiService by lazy { remoteDataSource.restApiService }

    override suspend fun performLogin(userName: String, password: String): Response<LoginResponse> {
        return restApiService.performLogin(LoginRequest(userName, password))
    }

    override fun setAuthToken(token: String) {
        jwtHelper.jwtToken = token
    }

    override suspend fun getDashboardData(dayNo: Int): Response<DashboardResponse> {
        return restApiService.getDashboardData(DashboardRequest(dayNo))
    }

    override suspend fun getConfigData(): Response<ConfigResponse> {
        return restApiService.getConfigData()
    }

    override suspend fun getRiskGradeConfigData(): Response<RiskGradeResponse> {
        return restApiService.getRiskGradeConfig()
    }

    override suspend fun getCascadeAddress(area: Int, id: Int): Response<List<CommonModel>> {
        return restApiService.getCascadeAddress(area, id)
    }

    override suspend fun getCustomerListData(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return restApiService.getCustomerListData(customerRequest)
    }

    override suspend fun getAccountListData(accountRequest: AccountRequest): Response<AccountResponse> {
        return restApiService.getAccountListData(accountRequest)
    }

    override suspend fun getSanctionScreeningData(sanctionScreeningRequest: SanctionScreeningRequest): Response<SanctionScreeningResponse> {
        return restApiService.getSanctionScreeningData(sanctionScreeningRequest)
    }

    override suspend fun getDedupeCheckData(dedupeCheckRequest: DedupeCheckRequest): Response<DedupeCheckResponse> {
        return restApiService.getDedupeCheckData(dedupeCheckRequest)
    }

    override suspend fun createCustomerData(createCustomerRequest: CreateCustomerRequest): Response<CreateCustomerResponse> {
        return restApiService.createCustomerData(createCustomerRequest)
    }

    override suspend fun getDepositData(commonRequest: CommonRequest): Response<Objects> {
        return restApiService.getDepositData(commonRequest)
    }

    override suspend fun getWithdrawData(commonRequest: CommonRequest): Response<Objects> {
        return restApiService.getWithdrawData(commonRequest)
    }

    override suspend fun getTransferData(commonRequest: CommonRequest): Response<Objects> {
        return restApiService.getTransferData(commonRequest)
    }

    override suspend fun getAccountConfigData(): Response<AccountConfigResponse> {
        return restApiService.getAccountConfig()
    }

    override suspend fun getTransactionProfileConfigData(): Response<List<TransactionProfileConfig>> {
        return restApiService.getTransactionProfileConfig()
    }

    override suspend fun getCustomerData(customerID: String): Response<CustomerDataResponse> {
        return restApiService.getCustomerData(customerID)
    }

    override suspend fun insert(generalInfo: GeneralInfo) {
        generalInfoDao.insert(generalInfo)
    }

    override fun getAll(): LiveData<List<GeneralInfo>> {
        return generalInfoDao.getAll()
    }
}