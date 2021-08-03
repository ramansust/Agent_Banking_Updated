package com.datasoft.abs.data.dto.createAccount.review

data class CreateAccountRequest(
    var accountTitle: String = "",
    var currencyId: Int = 0,
    var customerId: Int = 0,
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
    var transactionProfile: List<TransactionProfile> = emptyList()
)