package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.data.dto.createCustomer.AddressInfo
import com.datasoft.abs.data.dto.createCustomer.Contact
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NomineeViewModel @Inject constructor() : ViewModel() {

    private val nomineeAge = MutableLiveData<Int>()
    fun getNomineeAge(): LiveData<Int> = nomineeAge

    private val nomineeData = MutableLiveData<Resource<Nominee>>()
    fun getNomineeData(): LiveData<Resource<Nominee>> = nomineeData

    private val saveData = MutableLiveData<ArrayList<Nominee>>()
    fun getSavedData(): LiveData<ArrayList<Nominee>> = saveData

    fun checkData(
        fullName: String,
        motherName: String,
        spouseName: String,
        relation: Int,
        documentType: Int,
        expiryDate: String,
        presentAddress: String,
        fatherName: String,
        birthDate: String,
        percentShare: Int,
        occupation: Int,
        idValue: String,
        permanentAddress: String,
        applicant: String,
        photo: String,
        signature: String,
        docFront: String,
        docBack: String,
        nomineeName: String,
        nomineeBirthDate: String,
        nomineePermanentAddress: String,
        nomineeDocumentType: Int,
        nomineeExpiryDate: String,
        nomineeFatherSpouseName: String,
        nomineePresentAddress: String,
        nomineeWithRelation: Int,
        nomineeIdValue: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            nomineeData.postValue(Resource.Loading())

            if (fullName.isEmpty() || motherName.isEmpty()) {
                nomineeData.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            val nomineeInfo = Nominee(
                nomineeBirthDate,
                presentAddress,
                birthDate,
                documentType,
                "email",
                fatherName,
                "mobile",
                motherName,
                "NID",
                fullName,
                docBack,
                docFront,
                0,
                nomineePresentAddress,
                "",
                "",
                "",
                nomineePermanentAddress,
                0,
                "1205",
                photo,
                nomineeDocumentType,
                nomineeWithRelation,
                percentShare,
                signature
            )

            nomineeData.postValue(Resource.Success(nomineeInfo))
        }
    }

    fun nomineeAge(dob: String = "07.15.2011") {
        viewModelScope.launch(Dispatchers.IO) {

            val date = SimpleDateFormat(Constant.DATE_FORMAT, Locale.ENGLISH).parse(dob)
            val c: Calendar = Calendar.getInstance()
            c.time = date

            val today: Calendar = Calendar.getInstance()

            nomineeAge.postValue(
                today.get(Calendar.YEAR) - c.get(Calendar.YEAR)
            )
        }
    }

    fun notifyData(nomineeInfo: Nominee) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<Nominee> = ArrayList()
            list.add(nomineeInfo)
            saveData.value?.let {
                list.addAll(list.size - 1, it)
            }
            saveData.postValue(list)
        }
    }

    fun removeData(nomineeInfo: Nominee) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<Nominee> = ArrayList()
            saveData.value?.let {
                list.addAll(it)
            }
            list.remove(nomineeInfo)
            saveData.postValue(list)
        }
    }
}