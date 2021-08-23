package com.datasoft.abs.data

import androidx.lifecycle.LiveData
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.data.dto.accountList.AccountResponse
import com.datasoft.abs.data.dto.config.*
import com.datasoft.abs.data.dto.createAccount.general.CustomerDataResponse
import com.datasoft.abs.data.dto.createAccount.introducer.IntroducerInfo
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountRequest
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountResponse
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
import com.datasoft.abs.data.dto.transaction.*
import com.datasoft.abs.data.source.local.db.dao.GeneralInfoDao
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import com.datasoft.abs.data.source.remote.JwtHelper
import com.datasoft.abs.data.source.remote.RestRemoteDataSource
import com.datasoft.abs.domain.Repository
import retrofit2.Response
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

    override suspend fun getDepositData(commonRequest: CommonRequest): Response<DepositResponse> {
        return restApiService.getDepositData(commonRequest)
    }

    override suspend fun getWithdrawData(commonRequest: CommonRequest): Response<DepositResponse> {
        return restApiService.getWithdrawData(commonRequest)
    }

    override suspend fun getBalanceData(commonRequest: CommonRequest): Response<DepositResponse> {
        return restApiService.getTransferData(commonRequest)
    }

    override suspend fun getAccountConfigData(): Response<AccountConfigResponse> {
        return restApiService.getAccountConfig()
    }

    override suspend fun getTransactionProfileConfigData(productID: Int): Response<TransactionProfileConfig> {
        return restApiService.getTransactionProfileConfig(productID)
    }

    override suspend fun getCustomerData(customerID: String): Response<CustomerDataResponse> {
        return restApiService.getCustomerData(customerID)
    }

    override suspend fun getIntroducerData(accountNo: String): Response<IntroducerInfo> {
        return restApiService.getIntroducerData(accountNo)
    }

    override suspend fun createAccountData(createAccountRequest: CreateAccountRequest): Response<CreateAccountResponse> {
        return restApiService.createAccountData(createAccountRequest)
    }

    override suspend fun getAccountDetails(accountDetailsRequest: AccountDetailsRequest): Response<AccountDetailsResponse> {
        return restApiService.getAccountDetails(accountDetailsRequest)
    }

    override suspend fun getReceiverDetails(accountNumber: String): Response<ReceiverDetailsResponse> {
        return restApiService.getReceiverDetails(accountNumber)
    }

    override suspend fun getAmountDetails(amountDetailsRequest: AmountDetailsRequest): Response<AmountDetailsResponse> {
        return restApiService.getAmountDetails(amountDetailsRequest)
    }

    override suspend fun getWithdrawDeposit(withdrawDepositRequest: WithdrawDepositRequest): Response<WithdrawDepositResponse> {
        return restApiService.getWithdrawDeposit(withdrawDepositRequest)
    }

    override suspend fun getBalanceInquiry(accountNo: String): Response<BalanceInquiryResponse> {
        return restApiService.getBalanceInquiry(accountNo)
    }

    override suspend fun getTransactionDetails(transactionNo: String): Response<TransactionDetailsResponse> {
        return restApiService.getTransactionDetails(transactionNo)
    }

    override suspend fun insert(generalInfo: GeneralInfo) {
        generalInfoDao.insert(generalInfo)
    }

    override fun getAll(): LiveData<List<GeneralInfo>> {
        return generalInfoDao.getAll()
    }
}