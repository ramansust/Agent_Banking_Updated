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
import com.datasoft.abs.data.dto.profile.ChangePasswordRequest
import com.datasoft.abs.data.dto.profile.ChangePasswordResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.data.dto.transaction.*
import com.datasoft.abs.data.dto.transaction.eftn.CreateEFTNRequest
import com.datasoft.abs.data.dto.transaction.rtgs.CreateRequest
import com.datasoft.abs.data.dto.transaction.rtgs.Details
import com.datasoft.abs.data.dto.transaction.rtgs.RTGSListResponse
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.dao.customer.CustomerDao
import com.datasoft.abs.data.source.local.db.entity.account.Account
import com.datasoft.abs.data.source.local.db.entity.customer.*
import com.datasoft.abs.data.source.local.db.entity.customer.relation.*
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
    private val customerDao: CustomerDao,
    private val accountDao: AccountDao
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

    override suspend fun getChangePassword(changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse> {
        return restApiService.getChangePassword(changePasswordRequest)
    }

    override suspend fun getRTGSList(accountRequest: AccountRequest): Response<RTGSListResponse> {
        return restApiService.getRTGSList(accountRequest)
    }

    override suspend fun getEFTNList(accountRequest: AccountRequest): Response<RTGSListResponse> {
        return restApiService.getEFTNList(accountRequest)
    }

    override suspend fun getBankList(): Response<List<CommonModel>> {
        return restApiService.getBankList()
    }

    override suspend fun getBranchList(bankId: Int): Response<List<CommonModel>> {
        return restApiService.getBranchList(bankId)
    }

    override suspend fun createRTGSTransaction(createRequest: CreateRequest): Response<CreateCustomerResponse> {
        return restApiService.createRTGSTransaction(createRequest)
    }

    override suspend fun createEFTNTransaction(createRequest: CreateEFTNRequest): Response<CreateCustomerResponse> {
        return restApiService.createEFTNTransaction(createRequest)
    }

    override suspend fun getCashRegisterList(accountRequest: AccountRequest): Response<RTGSListResponse> {
        return restApiService.getCashRegisterList(accountRequest)
    }

    override suspend fun getFeederList(accountRequest: AccountRequest): Response<RTGSListResponse> {
        return restApiService.getFeederList(accountRequest)
    }

    override suspend fun createCashRegister(createRequest: CreateRequest): Response<CreateCustomerResponse> {
        return restApiService.createCashRegister(createRequest)
    }

    override suspend fun createFeeder(createRequest: CreateRequest): Response<CreateCustomerResponse> {
        return restApiService.createFeeder(createRequest)
    }

    override suspend fun getRTGSDetails(transactionID: String): Response<Details> {
        return restApiService.getRTGSDetails(transactionID)
    }

    override suspend fun getEFTNSDetails(transactionID: String): Response<Details> {
        return restApiService.getEFTNSDetails(transactionID)
    }

    override suspend fun insertGeneral(general: General) {
        customerDao.insertGeneral(general)
    }

    override suspend fun insertPersonal(personal: Personal) {
        customerDao.insertPersonal(personal)
    }

    override suspend fun insertNominee(nominee: Nominee) {
        customerDao.insertNominee(nominee)
    }

    override suspend fun insertGuardian(guardian: Guardian) {
        customerDao.insertGuardian(guardian)
    }

    override suspend fun insertAddress(address: Address) {
        customerDao.insertAddress(address)
    }

    override suspend fun insertPhoto(photo: Photo) {
        customerDao.insertPhoto(photo)
    }

    override suspend fun insertFingerprint(fingerprint: Fingerprint) {
        customerDao.insertFingerprint(fingerprint)
    }

    override suspend fun insertDocument(document: Document) {
        customerDao.insertDocument(document)
    }

    override suspend fun insertKYC(kyc: KYC) {
        customerDao.insertKYC(kyc)
    }

    override fun getAll(): LiveData<List<General>> {
        return customerDao.getAll()
    }

    override fun getGeneral(generalId: Int): General {
        return customerDao.getGeneral(generalId)
    }

    override fun getGeneralAndPersonal(generalId: Int): GeneralAndPersonal {
        return customerDao.getGeneralAndPersonal(generalId)
    }

    override fun getPersonalAndNominee(personalId: Int): PersonalAndNominee {
        return customerDao.getPersonalAndNominee(personalId)
    }

    override fun getPersonalAndGuardian(personalId: Int): PersonalAndGuardian {
        return customerDao.getPersonalAndGuardian(personalId)
    }

    override fun getGeneralWithAddresses(generalId: Int): List<GeneralWithAddresses> {
        return customerDao.getGeneralWithAddresses(generalId)
    }

    override fun getGeneralAndPhoto(generalId: Int): GeneralAndPhoto {
        return customerDao.getGeneralAndPhoto(generalId)
    }

    override fun getGeneralAndFingerprint(generalId: Int): GeneralAndFingerprint {
        return customerDao.getGeneralAndFingerprint(generalId)
    }

    override fun getGeneralWithDocuments(generalId: Int): List<GeneralWithDocuments> {
        return customerDao.getGeneralWithDocuments(generalId)
    }

    override fun getGeneralAndKYC(generalId: Int): GeneralAndKYC {
        return customerDao.getGeneralAndKYC(generalId)
    }

    override fun delete(generalId: Int) {
        customerDao.delete(generalId)
    }

    override suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    override fun getAccounts(): LiveData<List<Account>> {
        return accountDao.getAll()
    }
}