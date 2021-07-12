package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.PersonalInfo
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(): ViewModel() {

    private val personalData = MutableLiveData<Resource<PersonalInfo>>()
    fun getPersonalData(): LiveData<Resource<PersonalInfo>> = personalData

    fun checkData(
        maritalStatus: Int,
        spouseName: String,
        religion: Int,
        numberOfDependents: String,
        education: Int,
        occupation: Int,
        nationality: Int,
        birthCertificateNo: String,
        vatRegistrationNo: String,
        drivingLicense: String,
        monthlyIncome: String,
        sourceOfFund: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            personalData.postValue(Resource.Loading())

            if (sourceOfFund.isEmpty()) {
                personalData.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            val personalInfo = PersonalInfo(
                maritalStatus,
                spouseName,
                religion,
                numberOfDependents,
                education,
                occupation,
                nationality,
                birthCertificateNo,
                vatRegistrationNo,
                drivingLicense,
                monthlyIncome,
                sourceOfFund
            )

            personalData.postValue(Resource.Success(personalInfo))
        }
    }
}