package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.createCustomer.*
import com.datasoft.abs.databinding.FragmentReviewBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address.AddressViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents.DocumentsViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.fingerprint.FingerprintViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.general.GeneralViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc.KYCViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal.PersonalViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: ReviewViewModel by activityViewModels()
    private var _binding: FragmentReviewBinding? = null

    private val generalViewModel: GeneralViewModel by activityViewModels()
    private val personalViewModel: PersonalViewModel by activityViewModels()
    private val addressViewModel: AddressViewModel by activityViewModels()
    private val photoNIDViewModel: PhotoViewModel by activityViewModels()
    private val fingerprintViewModel: FingerprintViewModel by activityViewModels()
    private val documentsViewModel: DocumentsViewModel by activityViewModels()
    private val kycViewModel: KYCViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val createCustomerRequest = CreateCustomerRequest()
    private var guardian = GuardianInfo()
    private var nomineeInfo = EmergencyContact()
    private var kycInfo = KycInfoX()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        generalViewModel.getSavedData().observe(viewLifecycleOwner, {
            createCustomerRequest.apply {
                salutation = it.salutation
                firstName = it.firstName
                lastName = it.lastName
                dob = it.birthDate
                nid = it.nationalID
                gender = it.gender
                customerType = it.customerType
                mobile = it.mobileNumber
                motherName = it.motherName
                fatherName = it.fatherName
                //city & country
            }
        })

        addressViewModel.getSavedData().observe(viewLifecycleOwner, {
            val list = mutableListOf<Addresses>()
            for (addressInfo in it) {
                list.add(
                    Addresses(
                        addressInfo.houseNo,
                        addressInfo.addressType,
                        addressInfo.country,
                        addressInfo.districtValue,
                        addressInfo.district,
                        "",
                        0,
                        addressInfo.postCode,
                        "",
                        addressInfo.thanaValue,
                        addressInfo.thana,
                        addressInfo.unionValue,
                        addressInfo.union
                    )
                )
            }

            createCustomerRequest.addressess = list
        })

        addressViewModel.getContactData().observe(viewLifecycleOwner, {
            createCustomerRequest.contacts = it
        })

        photoNIDViewModel.getUserPhoto().observe(viewLifecycleOwner, {
            createCustomerRequest.profilePhoto = it
        })

        photoNIDViewModel.getSignature().observe(viewLifecycleOwner, {
            createCustomerRequest.signaturePhoto = it
        })

        photoNIDViewModel.getDocumentFront().observe(viewLifecycleOwner, {
            createCustomerRequest.nidFrontSide = it
        })

        photoNIDViewModel.getDocumentBack().observe(viewLifecycleOwner, {
            createCustomerRequest.nidBackSide = it
        })

        photoNIDViewModel.getGuardianPhoto().observe(viewLifecycleOwner, {
            guardian.profilePhoto = it
        })

        photoNIDViewModel.getGuardianSignature().observe(viewLifecycleOwner, {
            guardian.signaturePhoto = it
        })

        photoNIDViewModel.getGuardianDocumentFront().observe(viewLifecycleOwner, {
            guardian.docFrontSide = it
        })

        photoNIDViewModel.getGuardianDocumentBack().observe(viewLifecycleOwner, {
            guardian.docBackSide = it
        })

        photoNIDViewModel.getGuardianDocumentType().observe(viewLifecycleOwner, {
            guardian.documentTypeId = it
        })

        documentsViewModel.getSavedData().observe(viewLifecycleOwner, {
            createCustomerRequest.relatedDocs = it
        })

        kycViewModel.getKYCData().observe(viewLifecycleOwner, {
            kycInfo.apply {
                residentStatusId = it.residentStatus
                isBlackListedId = it.blackListed
                isPep = it.isPep
                isPepCloserId = it.isPepCloser
                isInterviewedPersonally = it.isInterviewedPersonally
                typeOfProductId = it.typeOfProduct
                professionOrNatureId = it.profession
                transparencyRiskId = it.transparencyRisk
            }

            createCustomerRequest.kycInfo = kycInfo
        })

        kycViewModel.getDocumentList().observe(viewLifecycleOwner, {
            for (verification in it) {
                if(verification.name == "Passport") {
                    kycInfo.isPassportNoReceived = verification.isPhotocopyCollected
                    kycInfo.isPassportNoVerified = verification.isVerified
                }
            }

            createCustomerRequest.kycInfo = kycInfo
        })

        personalViewModel.getPersonalData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        guardian.contactNo = it.guardianContact
                        guardian.dob = it.guardianDob
                        guardian.nameOfGuardian = it.guardianName
                        guardian.relationShipId = it.guardianRelation
                        guardian.permanentAddress = it.guardianAddress

                        nomineeInfo.address = it.nomineeAddress
                        nomineeInfo.email = it.nomineeEmail
                        nomineeInfo.mobileNo = it.nomineeMobile
                        nomineeInfo.name = it.nomineeName
                        nomineeInfo.relationshipId = it.nomineeRelation

                        createCustomerRequest.apply {
                            maritalStatus = it.maritalStatus
                            spouseName = it.spouseName
                            religion = it.religion
                            noOfDependent = it.numberOfDependents.toInt()
                            highestEducationalDegreeId = it.education
                            occupationId = it.occupation
                            nationalityId = it.nationality
                            birthCertificateNo = it.birthCertificateNo
                            vatRegistrationNo = it.vatRegistrationNo
                            drivingLicenseNo = it.drivingLicense
                            monthlyIncome = it.monthlyIncome.toInt()
                            sourceOfFund = it.sourceOfFund
                            guardianInfo = guardian
                            emergencyContact = nomineeInfo
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let {

                    }
                }

                is Resource.Loading -> {

                }
            }

        })

        viewModel.getCreateCustomerData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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

        binding.btnNext.setOnClickListener {
            Log.e("value", "" + createCustomerRequest)
//            viewModel.createCustomer(createCustomerRequest)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(6)
        }

        binding.btnGeneralEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(0)
        }

        binding.btnPersonalEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(1)
        }

        binding.btnAddressEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(2)
        }

        binding.btnPhotoNidEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
        }

        binding.btnFingerprintEidt.setOnClickListener {
            customerViewModel.requestCurrentStep(4)
        }

        binding.btnDocumentsEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }

        binding.btnKycEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(6)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}