package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.TpDetail
import com.datasoft.abs.data.dto.config.toTransactionProfile
import com.datasoft.abs.data.source.local.db.AccountInfo
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.entity.account.TransactionProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionProfileViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val accountInfo: AccountInfo
) : ViewModel() {

    private val transactionProfile = MutableLiveData<List<TpDetail>>()
    fun getTransactionProfile(): LiveData<List<TpDetail>> = transactionProfile

    fun setTransactionProfile(list: List<TpDetail>) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionProfile.postValue(list)
        }
    }

    fun insertTransactionProfile(transactionList: List<TpDetail>) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionList.map {
                val list = mutableListOf<TransactionProfile>()
                val profile = it.toTransactionProfile()
                profile.accountId = accountInfo.accountId
                list.add(profile)

                accountDao.insertTransactionProfiles(list)
            }
        }
    }
}