package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.source.local.db.AccountInfo
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.entity.account.OtherFacilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val accountInfo: AccountInfo
) : ViewModel() {

    private val notify = MutableLiveData<Boolean>()
    fun getNotifyData(): LiveData<Boolean> = notify

    private val chequeBook = MutableLiveData<Boolean>()
    fun getChequeBook(): LiveData<Boolean> = chequeBook

    private val smsBanking = MutableLiveData<Boolean>()
    fun getSMSBanking(): LiveData<Boolean> = smsBanking

    private val debitCard = MutableLiveData<Boolean>()
    fun getDebitCard(): LiveData<Boolean> = debitCard

    private val eStatement = MutableLiveData<Boolean>()
    fun getEStatement(): LiveData<Boolean> = eStatement

    private val internetBanking = MutableLiveData<Boolean>()
    fun getInternetBanking(): LiveData<Boolean> = internetBanking

    init {
        insertAll()
    }

    fun setChequeBook(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            chequeBook.postValue(value)
        }
    }

    fun setSMSBanking(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            smsBanking.postValue(value)
        }
    }

    fun setDebitCard(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            debitCard.postValue(value)
        }
    }

    fun setEStatement(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eStatement.postValue(value)
        }
    }

    fun setInternetBanking(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            internetBanking.postValue(value)
        }
    }

    fun setNotify(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notify.postValue(value)
        }
    }

    private fun insertAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<OtherFacilities>()

            val checkBook = OtherFacilities("Cheque Book", false)
            checkBook.accountId = accountInfo.accountId

            val debitCard = OtherFacilities("Debit Card", false)
            debitCard.accountId = accountInfo.accountId

            val smsBanking = OtherFacilities("SMS Banking", false)
            smsBanking.accountId = accountInfo.accountId

            val eStatement = OtherFacilities("e-Statement", false)
            eStatement.accountId = accountInfo.accountId

            val internetBanking = OtherFacilities("Internet Banking", false)
            internetBanking.accountId = accountInfo.accountId


            list.add(checkBook)
            list.add(debitCard)
            list.add(smsBanking)
            list.add(eStatement)
            list.add(internetBanking)

            accountDao.insertOthersFacilities(list)
        }
    }
}