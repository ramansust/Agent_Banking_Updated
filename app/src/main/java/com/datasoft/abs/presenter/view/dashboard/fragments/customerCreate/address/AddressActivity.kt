package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.AddressActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.AreaCode
import com.datasoft.abs.presenter.utils.Constant.ADDRESS_INFO
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressActivity : BaseActivity() {

    @Inject
    lateinit var toastHelper: ToastHelper

    private val customerViewModel: CustomerViewModel by viewModels()
    private val addressViewModel: AddressViewModel by viewModels()
    private lateinit var binding: AddressActivityBinding

    private val addressList = mutableListOf<CommonModel>()
    private val countryList = mutableListOf<CommonModel>()
    private val districtList = mutableListOf<CommonModel>()
    private val thanaList = mutableListOf<CommonModel>()
    private val unionList = mutableListOf<CommonModel>()
    private val contactList = mutableListOf<CommonModel>()

    override fun observeViewModel() {

        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        customerViewModel.getConfigData().observe(this, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        addressList.addAll(it.addessType)
                        binding.spinnerAddressType.adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_spinner_item,
                                addressList
                            )

                        countryList.addAll(it.nationalityList)
                        binding.spinnerCountry.adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_spinner_item,
                                countryList
                            )

                        districtList.addAll(it.districtList)
                        binding.spinnerDistrict.adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_spinner_item,
                                districtList
                            )

                        contactList.addAll(it.contactType)
                        binding.spinnerContactType.adapter =
                            ArrayAdapter(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactList
                            )
                    }
                }
                Status.ERROR -> {
                    response.message?.let { message ->
                        binding.btnSave.isFocusable = false
                        binding.btnSave.isClickable = false
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                Status.LOADING -> {
                }
            }
        })

        addressViewModel.getThanaData().observe(this, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {
                            thanaList.clear()
                            thanaList.addAll(it)
                            binding.spinnerThana.adapter =
                                ArrayAdapter(this, android.R.layout.simple_spinner_item, thanaList)
                        }
                    }

                    Status.ERROR -> {
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

        addressViewModel.getUnionData().observe(this, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {
                            unionList.clear()
                            unionList.addAll(it)
                            binding.spinnerUnion.adapter =
                                ArrayAdapter(this, android.R.layout.simple_spinner_item, unionList)
                        }
                    }

                    Status.ERROR -> {
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

        addressViewModel.getMessage().observe(this, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        val data = Intent()
                        data.putExtra(ADDRESS_INFO, result.data)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }

                    Status.ERROR -> {
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun initViewBinding() {
        binding = AddressActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customerViewModel.configData()

        binding.btnCross.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            addressViewModel.checkData(
                if (addressList.isNotEmpty()) addressList[binding.spinnerAddressType.selectedItemPosition].name else "",
                if (addressList.isNotEmpty()) addressList[binding.spinnerAddressType.selectedItemPosition].id else 0,
                binding.edTxtHouseNo.text.trim().toString(),
                binding.edTxtFlatNo.text.trim().toString(),
                binding.edTxtVillage.text.trim().toString(),
                binding.edTxtBlockNo.text.trim().toString(),
                binding.edTxtRoadNo.text.trim().toString(),
                binding.edTxtWordNo.text.trim().toString(),
                binding.edTxtZipCode.text.trim().toString(),
                binding.edTxtPostCode.text.trim().toString(),
                binding.edTxtState.text.trim().toString(),
                if (countryList.isNotEmpty()) countryList[binding.spinnerCountry.selectedItemPosition].id else 0,
                if (countryList.isNotEmpty()) countryList[binding.spinnerCountry.selectedItemPosition].name else "",
                binding.edTxtCity.text.trim().toString(),
                if (districtList.isNotEmpty()) districtList[binding.spinnerDistrict.selectedItemPosition].id else 0,
                if (districtList.isNotEmpty()) districtList[binding.spinnerDistrict.selectedItemPosition].name else "",
                if (thanaList.isNotEmpty()) thanaList[binding.spinnerThana.selectedItemPosition].id else 0,
                if (thanaList.isNotEmpty()) thanaList[binding.spinnerThana.selectedItemPosition].name else "",
                if (unionList.isNotEmpty()) unionList[binding.spinnerUnion.selectedItemPosition].id else 0,
                if (unionList.isNotEmpty()) unionList[binding.spinnerUnion.selectedItemPosition].name else "",
                if (contactList.isNotEmpty()) contactList[binding.spinnerContactType.selectedItemPosition].id else 0,
                binding.edTxtContactNo.text.trim().toString()
            )
        }

        binding.spinnerContactType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.edTxtContactNo.setText("")
                    if (contactList.isNotEmpty() && contactList[position].name.equals(
                            "email",
                            true
                        )
                    ) {
                        binding.edTxtContactNo.inputType =
                            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    } else {
                        binding.edTxtContactNo.inputType =
                            InputType.TYPE_CLASS_PHONE
                    }
                }
            }

        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    addressViewModel.thanaData(AreaCode.THANA.type, districtList[position].id)
                }
            }

        binding.spinnerThana.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                addressViewModel.unionData(AreaCode.UNION.type, thanaList[position].id)
            }
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}