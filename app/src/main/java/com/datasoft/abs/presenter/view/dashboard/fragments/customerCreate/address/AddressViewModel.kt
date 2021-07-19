package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.createCustomer.AddressInfo
import com.datasoft.abs.data.dto.createCustomer.Contact
import com.datasoft.abs.domain.Repository
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: Repository,
    private val network: Network
) : ViewModel() {

    private val thana = MutableLiveData<Resource<List<CommonModel>>>()
    fun getThanaData(): LiveData<Resource<List<CommonModel>>> = thana

    private val union = MutableLiveData<Resource<List<CommonModel>>>()
    fun getUnionData(): LiveData<Resource<List<CommonModel>>> = union

    private val saveData = MutableLiveData<ArrayList<AddressInfo>>()
    fun getSavedData(): LiveData<ArrayList<AddressInfo>> = saveData

    private val contactData = MutableLiveData<ArrayList<Contact>>()
    fun getContactData(): LiveData<ArrayList<Contact>> = contactData

    private val sendMessage = MutableLiveData<Resource<AddressInfo>>()
    fun getMessage(): LiveData<Resource<AddressInfo>> = sendMessage

    fun thanaData(area: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (network.isConnected()) {
                try {
                    val response = repository.getCascadeAddress(area, id)
                    thana.postValue(handleResponse(response))
                } catch (e: Exception) {
                    thana.postValue(
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                thana.postValue(
                    Resource.Error(
                        "No internet connection", null
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
                        Resource.Error(
                            "Something went wrong!", null
                        )
                    )
                    e.printStackTrace()
                }
            } else {
                union.postValue(
                    Resource.Error(
                        "No internet connection", null
                    )
                )
            }
        }
    }

    private fun handleResponse(response: Response<List<CommonModel>>): Resource<List<CommonModel>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun checkData(
        addressType: Int, houseNo: String, flatNo: String, village: String,
        blockNo: String, roadNo: String, wordNo: String, zipCode: String, postCode: String,
        state: String, country: Int, countryValue: String, city: String, district: Int, districtValue: String,
        thana: Int, thanaValue: String, union: Int, unionValue: String, contactType: Int, contactNo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            sendMessage.postValue(Resource.Loading())

            if (houseNo.isEmpty() || village.isEmpty() || postCode.isEmpty() || city.isEmpty() || contactNo.isEmpty()) {
                sendMessage.postValue(
                    Resource.Error(
                        "The fields must not be empty", null
                    )
                )
                return@launch
            }

            val addressInfo = AddressInfo(
                addressType, houseNo, flatNo, village,
                blockNo, roadNo, wordNo, zipCode, postCode, state, country, countryValue,
                city, district, districtValue, thana, thanaValue, union, unionValue, contactType, contactNo
            )

            sendMessage.postValue(Resource.Success(addressInfo))
        }
    }

    fun notifyData(addressInfo: AddressInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<AddressInfo> = ArrayList()
            list.add(addressInfo)
            saveData.value?.let {
                list.addAll(list.size - 1, it)
            }
            saveData.postValue(list)

            val contactList: ArrayList<Contact> = ArrayList()
            contactList.add(Contact(addressInfo.contactNo, addressInfo.contactType))
            contactData.value?.let {
                contactList.addAll(contactList.size - 1, it)
            }
            contactData.postValue(contactList)
        }
    }

    fun removeData(addressInfo: AddressInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val list: ArrayList<AddressInfo> = ArrayList()
            saveData.value?.let {
                list.addAll(it)
            }
            list.remove(addressInfo)
            saveData.postValue(list)

            val contactList: ArrayList<Contact> = ArrayList()
            contactData.value?.let {
                contactList.addAll(it)
            }
            contactList.remove(Contact(addressInfo.contactNo, addressInfo.contactType))
            contactData.postValue(contactList)
        }
    }

}