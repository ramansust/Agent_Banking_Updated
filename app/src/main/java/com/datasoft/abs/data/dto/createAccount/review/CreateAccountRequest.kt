package com.datasoft.abs.data.dto.createAccount.review

data class CreateAccountRequest(
    var accountTitle: String = "",
    var currencyId: Int = 0,
    var initialAmount: Int = 0,
    var introducerId: Int = 0,
    var isChequeBookEnable: Boolean = false,
    var isDebitCardEnable: Boolean = false,
    var isEStatementEnable: Boolean = false,
    var isInternetBankingEnable: Boolean = false,
    var isSmsBankingEnable: Boolean = false,
    var mandateOfAccOperationId: Int = 0,
    var natureOfBusinessId: Int = 0,
    var nominees: List<Nominee> = emptyList(),
    var productId: Int = 0,
    var relationWithIntroducer: Int = 0,
    var sectorCode: Int = 0,
    var transactionProfile: List<TransactionProfile> = emptyList(),
    var jointCustomer: List<JointCustomerInfo> = emptyList()
) {
    override fun toString(): String {
        return "CreateAccountRequest(accountTitle='$accountTitle', currencyId=$currencyId, initialAmount=$initialAmount, introducerId=$introducerId, isChequeBookEnable=$isChequeBookEnable, isDebitCardEnable=$isDebitCardEnable, isEStatementEnable=$isEStatementEnable, isInternetBankingEnable=$isInternetBankingEnable, isSmsBankingEnable=$isSmsBankingEnable, mandateOfAccOperationId=$mandateOfAccOperationId, natureOfBusinessId=$natureOfBusinessId, nominees=$nominees, productId=$productId, relationWithIntroducer=$relationWithIntroducer, sectorCode=$sectorCode, transactionProfile=$transactionProfile, jointCustomer=$jointCustomer)"
    }
}