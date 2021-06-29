package com.datasoft.abs.presenter.view.dashboard.fragments.customer.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
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

    private val savedData = MutableLiveData<DedupeCheckRequest>()
    fun getSavedData(): LiveData<DedupeCheckRequest> = savedData

    fun requestDedupeData(
        firstName: String,
        lastName: String,
        dob: String,
        nid: String,
        mobileNumber: String,
        fatherName: String,
        customerType: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            dedupeData.postValue(Resource.Loading())

            val dedupeRequest = DedupeCheckRequest(
                firstName = firstName,
                lastName = lastName,
                birthDate = dob,
                nationalID13Digit = nid,
                mobileNumber = mobileNumber,
                fatherName = fatherName,
                customerType = customerType
            )

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || nid.isEmpty() || mobileNumber.isEmpty()
                || fatherName.isEmpty()) {
                dedupeData.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            } else {
                savedData.postValue(dedupeRequest)
            }

            /*if (network.isConnected()) {
                try {
                    val response = repository.getDedupeCheckData(
                        dedupeRequest
                    )
                    dedupeData.postValue(handleDedupeResponse(response))
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
            }*/
        }
    }

    private fun handleDedupeResponse(response: Response<DedupeCheckResponse>): Resource<DedupeCheckResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}