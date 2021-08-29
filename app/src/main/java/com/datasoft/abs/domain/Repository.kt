package com.datasoft.abs.domain

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
import com.datasoft.abs.data.dto.dashboard.DashboardResponse
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.data.dto.profile.ChangePasswordRequest
import com.datasoft.abs.data.dto.profile.ChangePasswordResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.data.dto.transaction.*
import com.datasoft.abs.data.dto.transaction.eftn.CreateEFTNRequest
import com.datasoft.abs.data.dto.transaction.rtgs.CreateRequest
import com.datasoft.abs.data.dto.transaction.rtgs.Details
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.data.source.local.db.entity.GeneralInfo
import retrofit2.Response

interface Repository {
    suspend fun performLogin(userName: String, password: String): Response<LoginResponse>
    fun setAuthToken(token: String)
    suspend fun getDashboardData(dayNo: Int): Response<DashboardResponse>

    suspend fun getConfigData(): Response<ConfigResponse>
    suspend fun getRiskGradeConfigData(): Response<RiskGradeResponse>
    suspend fun getCascadeAddress(area: Int, id: Int): Response<List<CommonModel>>
    suspend fun getCustomerListData(customerRequest: CustomerRequest): Response<CustomerResponse>
    suspend fun getAccountListData(accountRequest: AccountRequest): Response<AccountResponse>
    suspend fun getSanctionScreeningData(sanctionScreeningRequest: SanctionScreeningRequest): Response<SanctionScreeningResponse>
    suspend fun getDedupeCheckData(dedupeCheckRequest: DedupeCheckRequest): Response<DedupeCheckResponse>

    suspend fun createCustomerData(createCustomerRequest: CreateCustomerRequest): Response<CreateCustomerResponse>
    suspend fun getDepositData(commonRequest: CommonRequest): Response<DepositResponse>
    suspend fun getWithdrawData(commonRequest: CommonRequest): Response<DepositResponse>
    suspend fun getBalanceData(commonRequest: CommonRequest): Response<DepositResponse>

    suspend fun getAccountConfigData(): Response<AccountConfigResponse>
    suspend fun getTransactionProfileConfigData(productID: Int): Response<TransactionProfileConfig>
    suspend fun getCustomerData(customerID: String): Response<CustomerDataResponse>
    suspend fun getIntroducerData(accountNo: String): Response<IntroducerInfo>
    suspend fun createAccountData(createAccountRequest: CreateAccountRequest): Response<CreateAccountResponse>

    suspend fun getAccountDetails(accountDetailsRequest: AccountDetailsRequest): Response<AccountDetailsResponse>
    suspend fun getReceiverDetails(accountNumber: String): Response<ReceiverDetailsResponse>
    suspend fun getAmountDetails(amountDetailsRequest: AmountDetailsRequest): Response<AmountDetailsResponse>
    suspend fun getWithdrawDeposit(withdrawDepositRequest: WithdrawDepositRequest): Response<WithdrawDepositResponse>
    suspend fun getBalanceInquiry(accountNo: String): Response<BalanceInquiryResponse>
    suspend fun getTransactionDetails(transactionNo: String): Response<TransactionDetailsResponse>
    suspend fun getChangePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    suspend fun getRTGSList(accountRequest: AccountRequest): Response<RTGSListResponse>
    suspend fun getEFTNList(accountRequest: AccountRequest): Response<RTGSListResponse>
    suspend fun getBankList(): Response<List<CommonModel>>
    suspend fun getBranchList(bankId: Int): Response<List<CommonModel>>
    suspend fun createRTGSTransaction(createRequest: CreateRequest): Response<CreateCustomerResponse>
    suspend fun createEFTNTransaction(createRequest: CreateEFTNRequest): Response<CreateCustomerResponse>

    suspend fun getCashRegisterList(accountRequest: AccountRequest): Response<RTGSListResponse>
    suspend fun getFeederList(accountRequest: AccountRequest): Response<RTGSListResponse>
    suspend fun createCashRegister(createRequest: CreateRequest): Response<CreateCustomerResponse>
    suspend fun createFeeder(createRequest: CreateRequest): Response<CreateCustomerResponse>

    suspend fun getRTGSDetails(transactionID: String): Response<Details>
    suspend fun getEFTNSDetails(transactionID: String): Response<Details>

    suspend fun insert(generalInfo: GeneralInfo)
    fun getAll(): LiveData<List<GeneralInfo>>
}