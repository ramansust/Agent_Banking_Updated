package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import androidx.lifecycle.*
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.createCustomer.AddressInfo
import com.datasoft.abs.data.dto.createCustomer.Contact
import com.datasoft.abs.data.dto.createCustomer.toAddress
import com.datasoft.abs.data.source.local.db.CustomerInfo
import com.datasoft.abs.data.source.local.db.entity.customer.toAddressInfo
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
class AddressViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network,
    private val customerInfo: CustomerInfo,
    @Named(Constant.NO_INTERNET) private val noInternet: String,
    @Named(Constant.SOMETHING_WRONG) private val somethingWrong: String,
    @Named(Constant.FIELD_EMPTY) private val fieldEmpty: String,
) : ViewModel() {

    private val thana = MutableLiveData<Event<Resource<List<CommonModel>>>>()
    fun getThanaData(): LiveData<Event<Resource<List<CommonModel>>>> = thana

    private val union = MutableLiveData<Event<Resource<List<CommonModel>>>>()
    fun getUnionData(): LiveData<Event<Resource<List<CommonModel>>>> = union

    private var addresses: LiveData<List<AddressInfo>> =
        repository.getAddress(customerInfo.customerId).map {
            it.map { address ->
                address.toAddressInfo()
            }
        }

    fun getAddresses(): LiveData<List<AddressInfo>> = addresses

    private val contactData = MutableLiveData<ArrayList<Contact>>()
    fun getContactData(): LiveData<ArrayList<Contact>> = contactData

    private val sendMessage = MutableLiveData<Event<Resource<AddressInfo>>>()
    fun getMessage(): LiveData<Event<Resource<AddressInfo>>> = sendMessage

    fun thanaData(area: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getCascadeAddress(area, id)
                    thana.postValue(handleResponse(response))
                } catch (e: Exception) {
                    thana.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                thana.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    fun unionData(area: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getCascadeAddress(area, id)
                    union.postValue(handleResponse(response))
                } catch (e: Exception) {
                    union.postValue(
                        Event(
                            Resource.error(
                                somethingWrong, null
                            )
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                union.postValue(
                    Event(
                        Resource.error(
                            noInternet, null
                        )
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<List<CommonModel>>): Event<Resource<List<CommonModel>>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.success(resultResponse))
            }
        }
        return Event(Resource.error(response.message(), null))
    }

    fun checkData(
        addressTypeValue: String,
        addressType: Int,
        houseNo: String,
        flatNo: String,
        village: String,
        blockNo: String,
        roadNo: String,
        wordNo: String,
        zipCode: String,
        postCode: String,
        state: String,
        country: Int,
        countryValue: String,
        city: String,
        district: Int,
        districtValue: String,
        thana: Int,
        thanaValue: String,
        union: Int,
        unionValue: String,
        contactType: Int,
        contactNo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            sendMessage.postValue(Event(Resource.loading(null)))

            if (houseNo.isEmpty() || village.isEmpty() || postCode.isEmpty() || city.isEmpty() || contactNo.isEmpty()) {
                sendMessage.postValue(
                    Event(
                        Resource.error(
                            fieldEmpty, null
                        )
                    )
                )
                return@launch
            }

            val addressInfo = AddressInfo(
                0,
                addressTypeValue,
                addressType,
                houseNo,
                flatNo,
                village,
                blockNo,
                roadNo,
                wordNo,
                zipCode,
                postCode,
                state,
                country,
                countryValue,
                city,
                district,
                districtValue,
                thana,
                thanaValue,
                union,
                unionValue,
                contactType,
                contactNo
            )

            val address = addressInfo.toAddress()
            address.generalId = customerInfo.customerId
            repository.insertAddress(address)

            sendMessage.postValue(Event(Resource.success(addressInfo)))
        }
    }

    fun removeAddress(addressId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAddress(addressId)
        }
    }
}