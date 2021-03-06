package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.review

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.createCustomer.*
import com.datasoft.abs.databinding.FragmentReviewBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address.AddressViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents.DocumentsViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.fingerprint.FingerprintViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.general.GeneralViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc.KYCViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal.PersonalViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var toastHelper: ToastHelper

    private val createCustomerRequest = CreateCustomerRequest()
    private var guardian = GuardianInfo()
    private var nomineeInfo = EmergencyContact()
    private var kycInfo = KycInfoX()

    private var isAdult = false

    @Inject
    lateinit var glide: RequestManager

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

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        personalViewModel.getCustomerAgeData().observe(viewLifecycleOwner, {
            if (it < Constant.ADULT_AGE) {
                isAdult = false
                binding.imgViewGuardianPhoto.visibility = View.VISIBLE
                binding.imgViewGuardianSignature.visibility = View.VISIBLE

                binding.txtViewGuardianPhoto.visibility = View.VISIBLE
                binding.txtViewGuardianSignature.visibility = View.VISIBLE

                binding.txtViewNidValue.visibility = View.GONE
                binding.txtViewMobileNoValue.visibility = View.GONE
                binding.txtViewFatherNameValue.visibility = View.GONE
                binding.txtViewMotherNameValue.visibility = View.GONE

                binding.txtViewNid.visibility = View.GONE
                binding.txtViewMobileNo.visibility = View.GONE
                binding.txtViewFatherName.visibility = View.GONE
                binding.txtViewMotherName.visibility = View.GONE

                binding.viewMiddle.visibility = View.VISIBLE

                binding.txtViewGuardianInfo.visibility = View.VISIBLE
                binding.txtViewGuardianName.visibility = View.VISIBLE
                binding.txtViewGuardianNameValue.visibility = View.VISIBLE
                binding.txtViewGuardianMobileNumber.visibility = View.VISIBLE
                binding.txtViewGuardianMobileNumberValue.visibility = View.VISIBLE
                binding.txtViewGuardianRelation.visibility = View.VISIBLE
                binding.txtViewGuardianRelationValue.visibility = View.VISIBLE
                binding.txtViewGuardianDob.visibility = View.VISIBLE
                binding.txtViewGuardianDobValue.visibility = View.VISIBLE
            } else if (it >= Constant.ADULT_AGE) {
                isAdult = true

                binding.imgViewGuardianPhoto.visibility = View.INVISIBLE
                binding.imgViewGuardianSignature.visibility = View.INVISIBLE

                binding.txtViewGuardianPhoto.visibility = View.INVISIBLE
                binding.txtViewGuardianSignature.visibility = View.INVISIBLE

                binding.txtViewNidValue.visibility = View.VISIBLE
                binding.txtViewMobileNoValue.visibility = View.VISIBLE
                binding.txtViewFatherNameValue.visibility = View.VISIBLE
                binding.txtViewMotherNameValue.visibility = View.VISIBLE

                binding.txtViewNid.visibility = View.VISIBLE
                binding.txtViewMobileNo.visibility = View.VISIBLE
                binding.txtViewFatherName.visibility = View.VISIBLE
                binding.txtViewMotherName.visibility = View.VISIBLE

                binding.viewMiddle.visibility = View.GONE

                binding.txtViewGuardianInfo.visibility = View.GONE
                binding.txtViewGuardianName.visibility = View.GONE
                binding.txtViewGuardianNameValue.visibility = View.GONE
                binding.txtViewGuardianMobileNumber.visibility = View.GONE
                binding.txtViewGuardianMobileNumberValue.visibility = View.GONE
                binding.txtViewGuardianRelation.visibility = View.GONE
                binding.txtViewGuardianRelationValue.visibility = View.GONE
                binding.txtViewGuardianDob.visibility = View.GONE
                binding.txtViewGuardianDobValue.visibility = View.GONE
            }
        })

        generalViewModel.getSanctionData().observe(viewLifecycleOwner, {
            createCustomerRequest.customerNo = it.customerNo
            createCustomerRequest.branchId = it.branchId
        })

        generalViewModel.getSavedData().observe(viewLifecycleOwner, {
            val fullName = it.firstName + " " + it.lastName
            binding.txtViewFullNameValue.text = fullName
            binding.txtViewDobValue.text = it.birthDate
            binding.txtViewNidValue.text = it.nationalID
            binding.txtViewMobileNoValue.text = it.mobileNumber
            binding.txtViewFatherNameValue.text = it.fatherName
            binding.txtViewMotherNameValue.text = it.motherName

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
            }
        })

        addressViewModel.getSavedData().observe(viewLifecycleOwner, {
            val list = mutableListOf<Addresses>()
            for (addressInfo in it) {
                list.add(
                    Addresses(
                        addressInfo.houseNo + ", " + addressInfo.village,
                        addressInfo.addressType,
                        addressInfo.country,
                        addressInfo.district,
                        addressInfo.postCode,
                        addressInfo.thana
                    )
                )
            }

            createCustomerRequest.addressess = list

            when {
                it.size > 1 -> {
                    binding.txtViewAddress1.visibility = View.VISIBLE
                    binding.txtViewAddress1Value.visibility = View.VISIBLE

                    binding.txtViewAddress2.visibility = View.VISIBLE
                    binding.txtViewAddress2Value.visibility = View.VISIBLE

                    binding.txtViewAddress1.text = it[0].addressTypeValue
                    val addressLine1 =
                        it[0].houseNo + ", " + it[0].village + ", " + it[0].districtValue + ", " + it[0].countryValue
                    binding.txtViewAddress1Value.text = addressLine1

                    binding.txtViewAddress2.text = it[1].addressTypeValue
                    val addressLine2 =
                        it[1].houseNo + ", " + it[1].village + ", " + it[1].districtValue + ", " + it[1].countryValue
                    binding.txtViewAddress2Value.text = addressLine2
                }

                it.size == 1 -> {
                    binding.txtViewAddress1.visibility = View.VISIBLE
                    binding.txtViewAddress1Value.visibility = View.VISIBLE

                    binding.txtViewAddress1.text = it[0].addressTypeValue
                    val addressLine =
                        it[0].houseNo + ", " + it[0].village + ", " + it[0].districtValue + ", " + it[0].countryValue
                    binding.txtViewAddress1Value.text = addressLine
                }
            }
        })

        addressViewModel.getContactData().observe(viewLifecycleOwner, {
            createCustomerRequest.contacts = it
        })

        photoNIDViewModel.getUserPhoto().observe(viewLifecycleOwner, {
            createCustomerRequest.profilePhoto = it

            glide.load(Base64.decode(it, Base64.DEFAULT)).into(binding.imgViewPhoto)
        })

        photoNIDViewModel.getSignature().observe(viewLifecycleOwner, {
            createCustomerRequest.signaturePhoto = it

            glide.load(Base64.decode(it, Base64.DEFAULT)).into(binding.imgViewSignature)
        })

        photoNIDViewModel.getDocumentFront().observe(viewLifecycleOwner, {
            createCustomerRequest.nidFrontSide = it
        })

        photoNIDViewModel.getDocumentBack().observe(viewLifecycleOwner, {
            createCustomerRequest.nidBackSide = it
        })

        photoNIDViewModel.getGuardianPhoto().observe(viewLifecycleOwner, {
            guardian.profilePhoto = it
            createCustomerRequest.guardianInfo = guardian

            glide.load(Base64.decode(it, Base64.DEFAULT)).into(binding.imgViewGuardianPhoto)
        })

        photoNIDViewModel.getGuardianSignature().observe(viewLifecycleOwner, {
            guardian.signaturePhoto = it
            createCustomerRequest.guardianInfo = guardian

            glide.load(Base64.decode(it, Base64.DEFAULT)).into(binding.imgViewGuardianSignature)
        })

        photoNIDViewModel.getGuardianDocumentFront().observe(viewLifecycleOwner, {
            guardian.docFrontSide = it
            createCustomerRequest.guardianInfo = guardian
        })

        photoNIDViewModel.getGuardianDocumentBack().observe(viewLifecycleOwner, {
            guardian.docBackSide = it
            createCustomerRequest.guardianInfo = guardian
        })

        photoNIDViewModel.getGuardianDocumentType().observe(viewLifecycleOwner, {
            guardian.documentTypeId = it
            createCustomerRequest.guardianInfo = guardian
        })

        documentsViewModel.getSavedData().observe(viewLifecycleOwner, {
            createCustomerRequest.relatedDocs = it

            when {
                it.size > 1 -> {
                    binding.txtViewDocument1.visibility = View.VISIBLE
                    binding.txtViewDocument1Value.visibility = View.VISIBLE

                    binding.txtViewDocument2.visibility = View.VISIBLE
                    binding.txtViewDocument2Value.visibility = View.VISIBLE

                    binding.txtViewDocument1.text = it[0].docTypeName
                    val document1 =
                        resources.getString(R.string.tracing_id) + ": " + it[0].tracingId + ", " + resources.getString(
                            R.string.expiry_date_
                        ) + ": " + it[0].expiredDate
                    binding.txtViewDocument1Value.text = document1

                    binding.txtViewDocument2.text = it[1].docTypeName
                    val document2 =
                        resources.getString(R.string.tracing_id) + ": " + it[1].tracingId + ", " + resources.getString(
                            R.string.expiry_date_
                        ) + ": " + it[1].expiredDate
                    binding.txtViewDocument2Value.text = document2
                }

                it.size == 1 -> {
                    binding.txtViewDocument1.visibility = View.VISIBLE
                    binding.txtViewDocument1Value.visibility = View.VISIBLE

                    binding.txtViewDocument1.text = it[0].docTypeName
                    val document1 =
                        resources.getString(R.string.tracing_id) + ": " + it[0].tracingId + ", " + resources.getString(
                            R.string.expiry_date_
                        ) + ": " + it[0].expiredDate
                    binding.txtViewDocument1Value.text = document1
                }
            }
        })

        fingerprintViewModel.getFingerList().observe(viewLifecycleOwner, {
            createCustomerRequest.fingerPrint = FingerPrint("", "", "", ", ", "", "")
        })

        kycViewModel.getKYCData().observe(viewLifecycleOwner, {
            kycInfo.apply {
                accountOpeningWayId = it.typeOfOnboarding
                residentStatusId = it.residentStatus
                isBlackListedId = it.blackListed
                isPep = it.isPep
                isPepCloserId = it.isPepCloser
                isInterviewedPersonally = it.isInterviewedPersonally
                typeOfProductId = it.typeOfProduct
                professionOrNatureId = it.profession
                transparencyRiskId = it.transparencyRisk
                kycRiskFactorId = it.transactionalRisk
            }

            createCustomerRequest.kycInfo = kycInfo
        })

        customerViewModel.getDocumentList().observe(viewLifecycleOwner, {
            kycInfo.isNidNoReceived = it[0].isPhotocopyCollected
            kycInfo.isNidNoVerified = it[0].isVerified

            kycInfo.isPassportNoReceived = it[1].isPhotocopyCollected
            kycInfo.isPassportNoVerified = it[1].isVerified

            kycInfo.isBirthCertificateNoReceived = it[2].isPhotocopyCollected
            kycInfo.isBirthCertificateNoVerified = it[2].isVerified

            kycInfo.isETinNoReceived = it[3].isPhotocopyCollected
            kycInfo.isETinNoVerified = it[3].isVerified

            kycInfo.isDrivingLicenseNoReceived = it[4].isPhotocopyCollected
            kycInfo.isDrivingLicenseNoVerified = it[4].isVerified

            kycInfo.isVatRegNoReceived = it[5].isPhotocopyCollected
            kycInfo.isVatRegNoVerified = it[5].isVerified

            kycInfo.isOrgRegNoReceived = it[6].isPhotocopyCollected
            kycInfo.isOrgRegNoVerified = it[6].isVerified

            kycInfo.isCertificateOfIncorporationReceived = it[7].isPhotocopyCollected
            kycInfo.isCertificateOfIncorporationVerified = it[7].isVerified

            createCustomerRequest.kycInfo = kycInfo

            var photocopy = ""
            var photocopyNot = ""
            var verified = ""
            var verifiedNot = ""

            for (info in it) {
                if (info.isPhotocopyCollected && binding.txtViewPhotocopyValue.visibility == View.GONE) {
                    binding.txtViewPhotocopyValue.visibility = View.VISIBLE
                } else if (!info.isPhotocopyCollected && binding.txtViewPhotocopyValueNot.visibility == View.GONE) {
                    binding.txtViewPhotocopyValueNot.visibility = View.VISIBLE
                }

                if (info.isPhotocopyCollected) {
                    photocopy += info.name + ", "
                } else {
                    photocopyNot += info.name + ", "
                }

                if (info.isVerified && binding.txtViewVerifiedValue.visibility == View.GONE) {
                    binding.txtViewVerifiedValue.visibility = View.VISIBLE
                } else if (!info.isVerified && binding.txtViewVerifiedValueNot.visibility == View.GONE) {
                    binding.txtViewVerifiedValueNot.visibility = View.VISIBLE
                }

                if (info.isVerified) {
                    verified += info.name + ", "
                } else {
                    verifiedNot += info.name + ", "
                }
            }

            if (photocopy.isNotEmpty()) {
                photocopy = photocopy.substring(0, photocopy.length - 2)
                binding.txtViewPhotocopyValue.text = photocopy
            }

            if (photocopyNot.isNotEmpty()) {
                photocopyNot = photocopyNot.substring(0, photocopyNot.length - 2)
                binding.txtViewPhotocopyValueNot.text = photocopyNot
            }

            if (verified.isNotEmpty()) {
                verified = verified.substring(0, verified.length - 2)
                binding.txtViewVerifiedValue.text = verified
            }

            if (verifiedNot.isNotEmpty()) {
                verifiedNot = verifiedNot.substring(0, verifiedNot.length - 2)
                binding.txtViewVerifiedValueNot.text = verifiedNot
            }
        })

        personalViewModel.getPersonalData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        binding.txtViewReligionValue.text = it.religionValue
                        binding.txtViewEducationValue.text = it.educationValue
                        binding.txtViewOccupationValue.text = it.occupationValue
                        binding.txtViewNationalityValue.text = it.nationalityValue
                        binding.txtViewMonthlyIncomeValue.text = it.monthlyIncome
                        binding.txtViewSourceOfFundValue.text = it.sourceOfFund

                        guardian.contactNo = it.guardianContact
                        guardian.dob = it.guardianDob
                        guardian.nameOfGuardian = it.guardianName
                        guardian.relationShipId = it.guardianRelation
                        guardian.permanentAddress = it.guardianAddress

                        binding.txtViewGuardianNameValue.text = it.guardianName
                        binding.txtViewGuardianMobileNumberValue.text = it.guardianContact
                        binding.txtViewGuardianRelationValue.text = it.guardianRelationValue
                        binding.txtViewGuardianDobValue.text = it.guardianDob

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

                            guardianInfo = if (isAdult)
                                guardian
                            else
                                null

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
                    goneProgressBar()
                    response.data?.let {
                        toastHelper.sendToast(it.message)
                    }
                }
                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        binding.btnNext.setOnClickListener {
            viewModel.createCustomer(createCustomerRequest)
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

        binding.btnFingerprintEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(4)
        }

        binding.btnDocumentsEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }

        binding.btnKycEdit.setOnClickListener {
            customerViewModel.requestCurrentStep(6)
        }

    }

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}