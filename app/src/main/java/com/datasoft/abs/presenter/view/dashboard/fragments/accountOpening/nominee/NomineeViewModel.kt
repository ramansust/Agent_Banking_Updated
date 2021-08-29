package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.data.dto.createAccount.review.NomineeRemainMinor
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NomineeViewModel @Inject constructor(
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val nomineeAge = MutableLiveData<Int>()
    fun getNomineeAge(): LiveData<Int> = nomineeAge

    private val sharePercent = MutableLiveData<Int>().apply { postValue(0) }
    fun getSharePercent(): LiveData<Int> = sharePercent

    private val nomineeData = MutableLiveData<Resource<Nominee>>()
    fun getNomineeData(): LiveData<Resource<Nominee>> = nomineeData

    private val saveData = MutableLiveData<ArrayList<Nominee>>()
    fun getSavedData(): LiveData<ArrayList<Nominee>> = saveData

    private val backImage = MutableLiveData<Boolean>()
    fun getBackImage(): LiveData<Boolean> = backImage

    fun checkData(
        name: String,
        fatherName: String,
        motherName: String,
        dob: String,
        spouseName: String,
        shareOfPercentage: Int,
        relationship: Int,
        relationshipValue: String,
        occupationId: Int,
        docTypeId: Int,
        nidNo: String,
        expiryDate: String,
        permanentAddress: String,
        presentAddress: String,
        applicant: String,
        photo: String,
        signaturePhoto: String,
        nidFrontPhoto: String,
        nidBackPhoto: String,
        nomineeName: String,
        nomineeFatherSpouseName: String,
        nomineeBirthDate: String,
        nomineePresentAddress: String,
        nomineePermanentAddress: String,
        nomineeWithRelation: Int,
        nomineeDocId: Int,
        nomineeIdValue: String,
        nomineeExpiryDate: String,
        isMinor: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            nomineeData.postValue(Resource.Loading())

            if (name.isEmpty() || motherName.isEmpty() || expiryDate.isEmpty() ||
                presentAddress.isEmpty() || fatherName.isEmpty() || dob.isEmpty() ||
                shareOfPercentage == 0 || nidNo.isEmpty() || permanentAddress.isEmpty() || presentAddress.isEmpty() ||
                photo.isEmpty() || signaturePhoto.isEmpty() || nidFrontPhoto.isEmpty()
            ) {
                nomineeData.postValue(
                    Resource.Error(
                        fieldEmpty, null
                    )
                )
                return@launch
            } else if (isMinor) {
                if (nomineeName.isEmpty() || nomineeBirthDate.isEmpty() || nomineePermanentAddress.isEmpty() ||
                    nomineeExpiryDate.isEmpty() || nomineeFatherSpouseName.isEmpty() || nomineePresentAddress.isEmpty() ||
                    nomineeIdValue.isEmpty()
                ) {
                    nomineeData.postValue(
                        Resource.Error(
                            fieldEmpty, null
                        )
                    )
                    return@launch
                }
            }

            val nomineeInfo = Nominee(
                name,
                fatherName,
                motherName,
                dob,
                spouseName,
                shareOfPercentage,
                relationship,
                relationshipValue,
                occupationId,
                docTypeId,
                nidNo,
                expiryDate,
                permanentAddress,
                presentAddress,
                applicant,
                photo,
                signaturePhoto,
                nidFrontPhoto,
                nidBackPhoto,
                if (isMinor) NomineeRemainMinor(
                    nomineeName,
                    nomineeFatherSpouseName,
                    nomineeBirthDate,
                    nomineePresentAddress,
                    nomineePermanentAddress,
                    nomineeWithRelation,
                    nomineeDocId,
                    nomineeIdValue,
                    nomineeExpiryDate
                ) else null
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
            setSharePercent(nomineeInfo.shareOfPercentage)
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
            removeSharePercent(nomineeInfo.shareOfPercentage)
        }
    }

    fun setBackImage(isRequired: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            backImage.postValue(isRequired)
        }
    }

    private fun setSharePercent(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sharePercent.postValue(sharePercent.value?.plus(value))
        }
    }

    private fun removeSharePercent(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            sharePercent.postValue(sharePercent.value?.minus(value))
        }
    }
}