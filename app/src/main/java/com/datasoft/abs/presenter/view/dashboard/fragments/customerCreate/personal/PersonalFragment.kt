package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal

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
import com.datasoft.abs.databinding.PersonalFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: PersonalViewModel by activityViewModels()
    private var _binding: PersonalFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PersonalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maritalList = mutableListOf<CommonModel>()
        val religionList = mutableListOf<CommonModel>()
        val educationList = mutableListOf<CommonModel>()
        val occupationList = mutableListOf<CommonModel>()
        val nationalityList = mutableListOf<CommonModel>()

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

//                        maritalList.addAll(it.m)
                        binding.spinnerMaritalStatus.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, maritalList)

                        religionList.addAll(it.religionList)
                        binding.spinnerReligion.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, religionList)

//                        educationList.addAll(it.ed)
                        binding.spinnerEducation.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, educationList)

                        occupationList.addAll(it.occupationList)
                        binding.spinnerOccupation.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, occupationList)

                        nationalityList.addAll(it.nationalityList)
                        binding.spinnerNationality.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nationalityList)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })


        viewModel.getPersonalData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        if(maritalList.isNotEmpty()) binding.spinnerMaritalStatus.setSelection(maritalList.indexOf(
                            CommonModel(it.maritalStatus)
                        ))
                        binding.edTxtSpouseName.setText(it.spouseName)
                        if(religionList.isNotEmpty()) binding.spinnerReligion.setSelection(religionList.indexOf(
                            CommonModel(it.religion)
                        ))
                        binding.edTxtDependent.setText(it.numberOfDependents)
                        if(educationList.isNotEmpty()) binding.spinnerEducation.setSelection(educationList.indexOf(
                            CommonModel(it.education)
                        ))
                        if(occupationList.isNotEmpty()) binding.spinnerOccupation.setSelection(occupationList.indexOf(
                            CommonModel(it.occupation)
                        ))
                        if(nationalityList.isNotEmpty()) binding.spinnerNationality.setSelection(nationalityList.indexOf(
                            CommonModel(it.nationality)
                        ))
                        binding.edTxtBirthCertificate.setText(it.birthCertificateNo)
                        binding.edTxtVat.setText(it.vatRegistrationNo)
                        binding.edTxtDrivingLicense.setText(it.drivingLicense)
                        binding.edTxtMonthlyIncome.setText(it.monthlyIncome)
                        binding.edTxtSourceOfFund.setText(it.sourceOfFund)

                        if(isClicked)
                            customerViewModel.requestCurrentStep(2)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                }
            }

        })

        binding.btnNext.setOnClickListener {
            isClicked = true
            viewModel.checkData(
                if(maritalList.isNotEmpty()) maritalList[binding.spinnerMaritalStatus.selectedItemPosition].id else 0,
                binding.edTxtSpouseName.text.trim().toString(),
                if(religionList.isNotEmpty()) religionList[binding.spinnerEducation.selectedItemPosition].id else 0,
                binding.edTxtDependent.text.trim().toString(),
                if(educationList.isNotEmpty()) educationList[binding.spinnerEducation.selectedItemPosition].id else 0,
                if(occupationList.isNotEmpty()) maritalList[binding.spinnerOccupation.selectedItemPosition].id else 0,
                if(nationalityList.isNotEmpty()) nationalityList[binding.spinnerNationality.selectedItemPosition].id else 0,
                binding.edTxtBirthCertificate.text.trim().toString(),
                binding.edTxtVat.text.trim().toString(),
                binding.edTxtDrivingLicense.text.trim().toString(),
                binding.edTxtMonthlyIncome.text.trim().toString(),
                binding.edTxtSourceOfFund.text.trim().toString()
            )
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}