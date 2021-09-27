package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckRequest
import com.datasoft.abs.data.dto.dedupecheck.DedupeCheckResponse
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningRequest
import com.datasoft.abs.data.dto.sanctionscreening.SanctionScreeningResponse
import com.datasoft.abs.data.source.local.db.entity.customer.General
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Event
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

    private val dedupeData = MutableLiveData<Event<Resource<DedupeCheckResponse>>>()
    fun getDedupeData(): LiveData<Event<Resource<DedupeCheckResponse>>> = dedupeData

    private val general = MutableLiveData<General>()
    fun getGeneral(): LiveData<General> = general

    private val sanction = MutableLiveData<SanctionScreeningResponse>()

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

            dedupeData.postValue(Event(Resource.loading(null)))

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || nid.isEmpty() || mobileNumber.isEmpty()
                || fatherName.isEmpty() || motherName.isEmpty() || city.isEmpty()
            ) {
                dedupeData.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
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
                    dedupeData.postValue(
                        handleDedupeResponse(
                            repository.getDedupeCheckData(dedupeRequest),
                            sanctionRequest,
                            salutation,
                            firstName,
                            lastName,
                            dob,
                            nid,
                            gender,
                            customerType,
                            mobileNumber,
                            motherName,
                            fatherName,
                            city,
                            nationalityId
                        )
                    )
                } catch (e: Exception) {
                    dedupeData.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                dedupeData.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleDedupeResponse(
        response: Response<DedupeCheckResponse>,
        sanctionScreeningRequest: SanctionScreeningRequest,
        salutation: Int?,
        firstName: String?,
        lastName: String?,
        dateBirth: String?,
        nid: String?,
        gender: Int?,
        customerType: Int?,
        mobileNumber: String?,
        motherName: String?,
        fatherName: String?,
        city: String?,
        country: Int?
    ): Event<Resource<DedupeCheckResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.message == "No Dedupe found in ABS") {
                    viewModelScope.launch(Dispatchers.IO) {
                        val response = repository.getSanctionScreeningData(sanctionScreeningRequest)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                sanction.postValue(it)

                                val general = General(
                                    salutation,
                                    firstName,
                                    lastName,
                                    dateBirth,
                                    nid,
                                    gender,
                                    customerType,
                                    mobileNumber,
                                    motherName,
                                    fatherName,
                                    city,
                                    country,
                                    sanction.value!!.branchId,
                                    sanction.value!!.customerNo
                                )

                                repository.insertGeneral(general)
                                return@let Event(Resource.success(response))
                            }
                        }
                    }
                    Event(Resource.success(resultResponse))
                } else
                    Event(Resource.error(resultResponse.message, null))
            }
        }
        return Event(Resource.error(response.message(), null))
    }
}