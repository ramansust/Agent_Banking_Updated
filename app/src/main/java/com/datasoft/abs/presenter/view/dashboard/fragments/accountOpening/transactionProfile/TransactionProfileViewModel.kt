package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.TpDetail
import com.datasoft.abs.data.source.local.db.dao.account.AccountDao
import com.datasoft.abs.data.source.local.db.entity.account.TransactionProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionProfileViewModel @Inject constructor(
    private val accountDao: AccountDao
) : ViewModel() {

    private val transactionProfile = MutableLiveData<List<TpDetail>>()
    fun getTransactionProfile(): LiveData<List<TpDetail>> = transactionProfile

    fun setTransactionProfile(list: List<TpDetail>) {
        viewModelScope.launch(Dispatchers.IO) {

            list.forEach {
                val transactionProfile = TransactionProfile(
                    it.profileName,
                    it.limitNoOfDailyTrn,
                    it.limitDailyTrnAmt,
                    it.limitNoOfMonthlyTrn,
                    it.limitMonthlyTrnAmt,
                    it.limitMaxTrnAmt
                )

                transactionProfile.accountId = 1

                accountDao.insertTransactionProfiles(transactionProfile)
            }

            transactionProfile.postValue(list)
        }
    }
}