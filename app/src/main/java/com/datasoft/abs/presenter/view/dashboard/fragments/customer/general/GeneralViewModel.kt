package com.datasoft.abs.presenter.view.dashboard.fragments.customer.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.dedupecheck.SaveData
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val dedupeData = MutableLiveData<Resource<DedupeCheckResponse>>()
    fun getDedupeData(): LiveData<Resource<DedupeCheckResponse>> = dedupeData

    private val savedData = MutableLiveData<SaveData>()
    fun getSavedData(): LiveData<SaveData> = savedData

    fun requestData(
        firstName: String,
        lastName: String,
        dob: String,
        nid: String,
        mobileNumber: String,
        fatherName: String,
        customerType: Int,
        gender: String,
        motherName: String,
        country: String,
        city: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            dedupeData.postValue(Resource.Loading())

            val saveData = SaveData(
                customerType,
                fatherName,
                firstName,
                lastName,
                dob,
                mobileNumber,
                nid,
                gender,
                motherName,
                country,
                city
            )
            savedData.postValue(saveData)

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || nid.isEmpty() || mobileNumber.isEmpty()
                || fatherName.isEmpty() || motherName.isEmpty() || gender.isEmpty() || country.isEmpty() || city.isEmpty()
            ) {
                dedupeData.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
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
                    "",
                    city,
                    country,
                    "$firstName $lastName",
                    customerType,
                    dob,
                    fatherName,
                    gender,
                    mobileNumber,
                    motherName,
                    nid
                )

                try {
                    val response = repository.getDedupeCheckData(dedupeRequest)
                    dedupeData.postValue(handleDedupeResponse(response, sanctionRequest))
                } catch (e: Exception) {
                    dedupeData.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                dedupeData.postValue(
                    Resource.Error(
                        "No internet connection", null
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
                                return@let if (it.responseCode == "200") {
                                    Resource.Success(resultResponse)
                                } else
                                    Resource.Error(resultResponse.message)
                            }
                        }
                    }
                    Resource.Error(resultResponse.message)
                } else
                    Resource.Error(resultResponse.message)
            }
        }
        return Resource.Error(response.message())
    }
}