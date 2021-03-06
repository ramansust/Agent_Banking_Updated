package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.dedupecheck.SaveData
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String
) : ViewModel() {

    private val dedupeData = MutableLiveData<Resource<DedupeCheckResponse>>()
    fun getDedupeData(): LiveData<Resource<DedupeCheckResponse>> = dedupeData

    private val savedData = MutableLiveData<SaveData>()
    fun getSavedData(): LiveData<SaveData> = savedData

    private val sanction = MutableLiveData<SanctionScreeningResponse>()
    fun getSanctionData(): LiveData<SanctionScreeningResponse> = sanction

    fun requestData(
        salutation: Int,
        firstName: String,
        lastName: String,
        dob: String,
        nid: String,
        gender: Int,
        mobileNumber: String,
        fatherName: String,
        customerType: Int,
        nationalityId: Int,
        motherName: String,
        city: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            dedupeData.postValue(Resource.Loading())

            val saveData = SaveData(
                salutation,
                customerType,
                fatherName,
                firstName,
                lastName,
                dob,
                mobileNumber,
                nid,
                nationalityId,
                gender,
                motherName,
                city
            )
            savedData.postValue(saveData)

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || nid.isEmpty() || mobileNumber.isEmpty()
                || fatherName.isEmpty() || motherName.isEmpty() || city.isEmpty()
            ) {
                dedupeData.postValue(
                    Resource.Error(
                        fieldEmpty, null
                    )
                )
                return@launch
            }

            if (network.isConnected()) {

                val dedupeRequest = DedupeCheckRequest(
                    firstName = firstName,
                    lastName = lastName,
                    birthDate = dob,
                    nationalID = nid,
                    mobileNumber = mobileNumber,
                    fatherName = fatherName,
                    customerType = customerType
                )

                val sanctionRequest = SanctionScreeningRequest(
                    firstName,
                    lastName,
                    fatherName,
                    mobileNumber,
                    city,
                    "$customerType",
                    dob,
                    motherName,
                    nid,
                    nationalityId
                )

                try {
                    val response = repository.getDedupeCheckData(dedupeRequest)
                    dedupeData.postValue(handleDedupeResponse(response, sanctionRequest))
                } catch (e: Exception) {
                    dedupeData.postValue(
                        Resource.Error(
                            somethingWrong, null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                dedupeData.postValue(
                    Resource.Error(
                        noInternet, null
                    )
                )
            }
        }
    }

    private fun handleDedupeResponse(
        response: Response<DedupeCheckResponse>,
        sanctionScreeningRequest: SanctionScreeningRequest
    ): Resource<DedupeCheckResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.message == "No Dedupe found in ABS") {
                    viewModelScope.launch(Dispatchers.IO) {
                        val response = repository.getSanctionScreeningData(sanctionScreeningRequest)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                sanction.postValue(it)
                                return@let Resource.Success(response)
                            }
                        }
                    }
                    Resource.Success(resultResponse)
                } else
                    Resource.Error(resultResponse.message)
            }
        }
        return Resource.Error(response.message())
    }
}