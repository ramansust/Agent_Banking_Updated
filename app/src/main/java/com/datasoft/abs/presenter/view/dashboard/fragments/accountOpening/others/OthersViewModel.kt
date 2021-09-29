package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.source.local.db.AccountInfo
import com.datasoft.abs.data.source.local.db.entity.account.OtherFacilities
import com.datasoft.abs.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val accountInfo: AccountInfo,
    private val repository: Repository
) : ViewModel() {

    private val notify = MutableLiveData<Boolean>()
    fun getNotifyData(): LiveData<Boolean> = notify

    private var otherFacilities: LiveData<List<OtherFacilities>> =
        repository.getOtherFacilities(accountInfo.accountId)

    fun getOtherFacilities(): LiveData<List<OtherFacilities>> = otherFacilities

    init {
        insertAll()
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

            repository.insertOthersFacilities(list)
        }
    }

    fun updateOtherFacility(id: Int, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOtherFacility(isChecked, id)
        }
    }
}