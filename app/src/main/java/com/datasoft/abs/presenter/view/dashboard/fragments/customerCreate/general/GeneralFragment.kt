package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.general

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.GeneralFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal.PersonalViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: GeneralViewModel by activityViewModels()
    private val personalViewModel: PersonalViewModel by activityViewModels()
    private var _binding: GeneralFragmentBinding? = null

    private val myCalendar: Calendar = Calendar.getInstance()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GeneralFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val salutationList = mutableListOf<CommonModel>()
        val genderList = mutableListOf<CommonModel>()
        val customerList = mutableListOf<CommonModel>()
        val countryList = mutableListOf<CommonModel>()

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        salutationList.addAll(it.salutationList)
                        binding.spinnerSalutation.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, salutationList)

                        genderList.addAll(it.genderList)
                        binding.spinnerGender.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderList)

                        customerList.addAll(it.customerTypeList)
                        binding.spinnerCustomerType.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, customerList)

                        countryList.addAll(it.nationalityList)
                        binding.spinnerCountry.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countryList)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        binding.btnNext.isEnabled = false
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })


        viewModel.getSavedData().observe(viewLifecycleOwner, { response ->
            if(salutationList.isNotEmpty()) binding.spinnerSalutation.setSelection(salutationList.indexOf(CommonModel(response.salutation)))
            binding.edTxtFirstName.setText(response.firstName)
            binding.edTxtLastName.setText(response.lastName)
            binding.edTxtDob.setText(response.birthDate)
            binding.edTxtNid.setText(response.nationalID)
            binding.edTxtMobileNumber.setText(response.mobileNumber)
            binding.edTxtFatherName.setText(response.fatherName)
            if(customerList.isNotEmpty()) binding.spinnerCustomerType.setSelection(customerList.indexOf(CommonModel(response.customerType)))
            if(countryList.isNotEmpty()) binding.spinnerCountry.setSelection(countryList.indexOf(CommonModel(response.nationalityId)))
            if(genderList.isNotEmpty()) binding.spinnerGender.setSelection(genderList.indexOf(CommonModel(response.gender)))
            binding.edTxtMotherName.setText(response.motherName)
            binding.edTxtCity.setText(response.city)
        })

        viewModel.getDedupeData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
//                    goneProgressBar()
                    response.data?.let {
                        if(isChecked)
                            customerViewModel.requestCurrentStep(1)
                    }
                }
                is Resource.Error -> {
//                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        })

        binding.edTxtDob.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(), date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        binding.btnNext.setOnClickListener {
            isChecked = true
            viewModel.requestData(
                if(salutationList.isNotEmpty()) salutationList[binding.spinnerSalutation.selectedItemPosition].id else 0,
                binding.edTxtFirstName.text.trim().toString(),
                binding.edTxtLastName.text.trim().toString(),
                binding.edTxtDob.text.trim().toString(),
                binding.edTxtNid.text.trim().toString(),
                if(genderList.isNotEmpty()) salutationList[binding.spinnerGender.selectedItemPosition].id else 0,
                binding.edTxtMobileNumber.text.trim().toString(),
                binding.edTxtFatherName.text.trim().toString(),
                if(customerList.isNotEmpty()) customerList[binding.spinnerCustomerType.selectedItemPosition].id else 0,
                if(countryList.isNotEmpty()) countryList[binding.spinnerCountry.selectedItemPosition].id else 0,
                binding.edTxtMotherName.text.trim().toString(),
                binding.edTxtCity.text.trim().toString()
            )
        }
    }

    private var date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        binding.edTxtDob.setText(sdf.format(myCalendar.time))
        personalViewModel.customerAge(binding.edTxtDob.text.trim().toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}