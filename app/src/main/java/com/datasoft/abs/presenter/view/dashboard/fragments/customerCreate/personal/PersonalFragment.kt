package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.PersonalFragmentBinding
import com.datasoft.abs.presenter.states.Status.*
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.ADULT_AGE
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PersonalFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: PersonalViewModel by activityViewModels()
    private var _binding: PersonalFragmentBinding? = null

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var isAgeAboveEighteen = false
    private val myCalendar: Calendar = Calendar.getInstance()

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
        val relationList = mutableListOf<CommonModel>()

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        viewModel.getCustomerAgeData().observe(viewLifecycleOwner, {

            (it < ADULT_AGE).apply {
                isAgeAboveEighteen = this
                binding.txtViewGuardian.isVisible = this
                binding.linearLayoutGuardian.isVisible = this
                binding.linearLayoutGuardian2.isVisible = this
            }
        })

        viewModel.getCustomerDobData().observe(viewLifecycleOwner, {
            binding.edTxtMinorAttainsDate.setText(it)
        })

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                SUCCESS -> {
                    response.data?.let {

                        maritalList.addAll(it.maritalStatusList)
                        binding.spinnerMaritalStatus.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                maritalList
                            )

                        religionList.addAll(it.religionList)
                        binding.spinnerReligion.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                religionList
                            )

                        educationList.addAll(it.educationList)
                        binding.spinnerEducation.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                educationList
                            )

                        occupationList.addAll(it.occupationList)
                        binding.spinnerOccupation.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                occupationList
                            )

                        nationalityList.addAll(it.nationalityList)
                        binding.spinnerNationality.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                nationalityList
                            )

                        relationList.addAll(it.relationList)
                        binding.spinnerNomineeRelation.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                relationList
                            )

                        binding.spinnerGuardianRelation.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                relationList
                            )
                    }
                }
                ERROR -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                LOADING -> {
                }
            }
        })

        viewModel.getPersonalAndNominee().let {
            if (maritalList.isNotEmpty()) binding.spinnerMaritalStatus.setSelection(
                maritalList.indexOf(
                    CommonModel(it.personal.maritalStatus!!)
                )
            )

            binding.edTxtSpouseName.setText(it.personal.spouseName)
            if (religionList.isNotEmpty()) binding.spinnerReligion.setSelection(
                religionList.indexOf(
                    CommonModel(it.personal.religion!!)
                )
            )
            binding.edTxtDependent.setText(it.personal.noOfDependencies!!.toString())
            if (educationList.isNotEmpty()) binding.spinnerEducation.setSelection(
                educationList.indexOf(
                    CommonModel(it.personal.education!!)
                )
            )
            if (occupationList.isNotEmpty()) binding.spinnerOccupation.setSelection(
                occupationList.indexOf(
                    CommonModel(it.personal.occupation!!)
                )
            )
            if (nationalityList.isNotEmpty()) binding.spinnerNationality.setSelection(
                nationalityList.indexOf(
                    CommonModel(it.personal.nationality!!)
                )
            )
            if (relationList.isNotEmpty()) binding.spinnerNomineeRelation.setSelection(
                relationList.indexOf(
                    CommonModel(it.nominee.relation!!)
                )
            )

            binding.edTxtBirthCertificate.setText(it.personal.birthCertificateNo)
            binding.edTxtVat.setText(it.personal.vatRegistration)
            binding.edTxtDrivingLicense.setText(it.personal.drivingLicense)
            binding.edTxtMonthlyIncome.setText(it.personal.monthIncome)
            binding.edTxtSourceOfFund.setText(it.personal.sourceFund)

            binding.edTxtName.setText(it.nominee.name)
            binding.edTxtMobileNumber.setText(it.nominee.mobileNumber)
            binding.edTxtAddress.setText(it.nominee.address)
            binding.edTxtEmail.setText(it.nominee.emailAddress)
        }

        viewModel.getPersonalAndGuardian().let {
            if (relationList.isNotEmpty()) binding.spinnerGuardianRelation.setSelection(
                relationList.indexOf(
                    CommonModel(it.guardian.relation!!)
                )
            )

            binding.edTxtGuardianName.setText(it.guardian.name)
            binding.edTxtGuardianContact.setText(it.guardian.contactNumber)
            binding.edTxtGuardianAddress.setText(it.guardian.address)
            binding.edTxtGuardianDob.setText(it.guardian.birthDate)
        }

        viewModel.getPersonalData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    SUCCESS -> {
                        result.data?.let {
                            customerViewModel.requestCurrentStep(2)
                        }
                    }
                    ERROR -> {
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }
                    LOADING -> {
                    }
                }
            }

            response?.peekContent()?.let { resultSave ->

                when (resultSave.status) {
                    SUCCESS -> {
                        resultSave.data?.let {

                            if (maritalList.isNotEmpty()) binding.spinnerMaritalStatus.setSelection(
                                maritalList.indexOf(
                                    CommonModel(it.maritalStatus)
                                )
                            )
                            binding.edTxtSpouseName.setText(it.spouseName)
                            if (religionList.isNotEmpty()) binding.spinnerReligion.setSelection(
                                religionList.indexOf(
                                    CommonModel(it.religion)
                                )
                            )
                            binding.edTxtDependent.setText(it.numberOfDependents)
                            if (educationList.isNotEmpty()) binding.spinnerEducation.setSelection(
                                educationList.indexOf(
                                    CommonModel(it.education)
                                )
                            )
                            if (occupationList.isNotEmpty()) binding.spinnerOccupation.setSelection(
                                occupationList.indexOf(
                                    CommonModel(it.occupation)
                                )
                            )
                            if (nationalityList.isNotEmpty()) binding.spinnerNationality.setSelection(
                                nationalityList.indexOf(
                                    CommonModel(it.nationality)
                                )
                            )
                            if (relationList.isNotEmpty()) binding.spinnerNomineeRelation.setSelection(
                                relationList.indexOf(
                                    CommonModel(it.nomineeRelation)
                                )
                            )
                            if (relationList.isNotEmpty()) binding.spinnerGuardianRelation.setSelection(
                                relationList.indexOf(
                                    CommonModel(it.guardianRelation)
                                )
                            )
                            binding.edTxtBirthCertificate.setText(it.birthCertificateNo)
                            binding.edTxtVat.setText(it.vatRegistrationNo)
                            binding.edTxtDrivingLicense.setText(it.drivingLicense)
                            binding.edTxtMonthlyIncome.setText(it.monthlyIncome)
                            binding.edTxtSourceOfFund.setText(it.sourceOfFund)

                            binding.edTxtName.setText(it.nomineeName)
                            binding.edTxtMobileNumber.setText(it.nomineeMobile)
                            binding.edTxtAddress.setText(it.nomineeAddress)
                            binding.edTxtEmail.setText(it.nomineeEmail)

                            binding.edTxtGuardianName.setText(it.guardianName)
                            binding.edTxtGuardianContact.setText(it.guardianContact)
                            binding.edTxtGuardianAddress.setText(it.guardianAddress)
                            binding.edTxtGuardianDob.setText(it.guardianDob)
                        }
                    }
                    ERROR -> {
                    }
                    LOADING -> {
                    }
                }
            }
        })

        binding.edTxtGuardianDob.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(), date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )

            val guardianCalender = Calendar.getInstance()
            guardianCalender.set(
                guardianCalender[Calendar.YEAR] - ADULT_AGE,
                guardianCalender[Calendar.MONDAY],
                guardianCalender[Calendar.DAY_OF_MONTH]
            )

            datePicker.datePicker.maxDate = guardianCalender.timeInMillis
            datePicker.show()
        }

        binding.btnNext.setOnClickListener {
            viewModel.checkData(
                isAgeAboveEighteen,
                if (maritalList.isNotEmpty()) maritalList[binding.spinnerMaritalStatus.selectedItemPosition].id else 0,
                binding.edTxtSpouseName.text.trim().toString(),
                if (religionList.isNotEmpty()) religionList[binding.spinnerReligion.selectedItemPosition].id else 0,
                if (religionList.isNotEmpty()) religionList[binding.spinnerReligion.selectedItemPosition].name else "",
                binding.edTxtDependent.text.trim().toString(),
                if (educationList.isNotEmpty()) educationList[binding.spinnerEducation.selectedItemPosition].id else 0,
                if (educationList.isNotEmpty()) educationList[binding.spinnerEducation.selectedItemPosition].name else "",
                if (occupationList.isNotEmpty()) occupationList[binding.spinnerOccupation.selectedItemPosition].id else 0,
                if (occupationList.isNotEmpty()) occupationList[binding.spinnerOccupation.selectedItemPosition].name else "",
                if (nationalityList.isNotEmpty()) nationalityList[binding.spinnerNationality.selectedItemPosition].id else 0,
                if (nationalityList.isNotEmpty()) nationalityList[binding.spinnerNationality.selectedItemPosition].name else "",
                binding.edTxtBirthCertificate.text.trim().toString(),
                binding.edTxtVat.text.trim().toString(),
                binding.edTxtDrivingLicense.text.trim().toString(),
                binding.edTxtMonthlyIncome.text.trim().toString(),
                binding.edTxtSourceOfFund.text.trim().toString(),

                binding.edTxtName.text.trim().toString(),
                binding.edTxtMobileNumber.text.trim().toString(),
                binding.edTxtAddress.text.trim().toString(),
                if (relationList.isNotEmpty()) relationList[binding.spinnerNomineeRelation.selectedItemPosition].id else 0,
                binding.edTxtEmail.text.trim().toString(),

                binding.edTxtGuardianName.text.trim().toString(),
                if (relationList.isNotEmpty()) relationList[binding.spinnerGuardianRelation.selectedItemPosition].id else 0,
                if (relationList.isNotEmpty()) relationList[binding.spinnerGuardianRelation.selectedItemPosition].name else "",
                binding.edTxtGuardianContact.text.trim().toString(),
                binding.edTxtGuardianAddress.text.trim().toString(),
                binding.edTxtGuardianDob.text.trim().toString()
            )
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(0)
        }
    }

    private var date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val sdf = SimpleDateFormat(Constant.DATE_FORMAT, Locale.US)
        binding.edTxtGuardianDob.setText(sdf.format(myCalendar.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}