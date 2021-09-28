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

    fun setChequeBook(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            chequeBook.postValue(value)
            insert("Cheque Book", value)
        }
    }

    fun setSMSBanking(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            smsBanking.postValue(value)
            insert("SMS Banking", value)
        }
    }

    fun setDebitCard(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            debitCard.postValue(value)
            insert("Debit Card", value)
        }
    }

    fun setEStatement(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            eStatement.postValue(value)
            insert("eStatement", value)
        }
    }

    fun setInternetBanking(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            internetBanking.postValue(value)
            insert("Internet Banking", value)
        }
    }

    fun setNotify(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            notify.postValue(value)
        }
    }

    private fun insert(title: String, value: Boolean) {
        val otherFacilities = OtherFacilities(
            title,
            value
        )
        otherFacilities.accountId = accountInfo.accountId
        accountDao.insertOthersFacilities(otherFacilities)
    }
}