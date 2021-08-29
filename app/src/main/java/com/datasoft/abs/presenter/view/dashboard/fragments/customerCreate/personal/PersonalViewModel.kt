package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createCustomer.PersonalInfo
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PersonalViewModel @Inject constructor(
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val customerAge = MutableLiveData<Int>()
    fun getCustomerAgeData(): LiveData<Int> = customerAge

    private val customerDob = MutableLiveData<String>()
    fun getCustomerDobData(): LiveData<String> = customerDob

    private val personalData = MutableLiveData<Resource<PersonalInfo>>()
    fun getPersonalData(): LiveData<Resource<PersonalInfo>> = personalData

    fun checkData(
        isAgeAboveEighteen: Boolean,
        maritalStatus: Int,
        spouseName: String,
        religion: Int,
        religionValue: String,
        numberOfDependents: String,
        education: Int,
        educationValue: String,
        occupation: Int,
        occupationValue: String,
        nationality: Int,
        nationalityValue: String,
        birthCertificateNo: String,
        vatRegistrationNo: String,
        drivingLicense: String,
        monthlyIncome: String,
        sourceOfFund: String,
        nomineeName: String,
        nomineeMobile: String,
        nomineeAddress: String,
        nomineeRelation: Int,
        nomineeEmail: String,
        guardianName: String,
        guardianRelation: Int,
        guardianRelationValue: String,
        guardianContact: String,
        guardianAddress: String,
        guardianDob: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            personalData.postValue(Resource.Loading())

            if (sourceOfFund.isEmpty() || nomineeName.isEmpty() || nomineeMobile.isEmpty() || nomineeAddress.isEmpty()) {
                personalData.postValue(
                    Resource.Error(
                        fieldEmpty, null
                    )
                )
                return@launch
            } else if (isAgeAboveEighteen && (guardianName.isEmpty() || guardianContact.isEmpty() || guardianDob.isEmpty())) {
                personalData.postValue(
                    Resource.Error(
                        fieldEmpty, null
                    )
                )
                return@launch
            }

            val personalInfo = PersonalInfo(
                maritalStatus,
                spouseName,
                religion,
                religionValue,
                numberOfDependents,
                education,
                educationValue,
                occupation,
                occupationValue,
                nationality,
                nationalityValue,
                birthCertificateNo,
                vatRegistrationNo,
                drivingLicense,
                monthlyIncome,
                sourceOfFund,
                nomineeName,
                nomineeMobile,
                nomineeAddress,
                nomineeRelation,
                nomineeEmail,
                guardianName,
                guardianRelation,
                guardianRelationValue,
                guardianContact,
                guardianAddress,
                guardianDob
            )

            personalData.postValue(Resource.Success(personalInfo))
        }
    }

    fun customerAge(dob: String = "07.15.2011") {
        viewModelScope.launch(Dispatchers.IO) {

            customerDob.postValue(dob)

            val date = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dob)
            val c: Calendar = Calendar.getInstance()
            c.time = date

            val today: Calendar = Calendar.getInstance()

            customerAge.postValue(
                today.get(Calendar.YEAR) - c.get(Calendar.YEAR)
            )
        }
    }
}