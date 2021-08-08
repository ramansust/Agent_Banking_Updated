package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountRequest
import com.datasoft.abs.data.dto.createAccount.review.JointCustomerInfo
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.data.dto.createAccount.review.TransactionProfile
import com.datasoft.abs.databinding.FragmentAccountReviewBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general.GeneralViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer.IntroducerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee.NomineeViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others.OthersViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile.TransactionProfileViewModel
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by activityViewModels()
    private val othersViewModel: OthersViewModel by activityViewModels()
    private val nomineeViewModel: NomineeViewModel by activityViewModels()
    private val introducerViewModel: IntroducerViewModel by activityViewModels()
    private val transactionViewModel: TransactionProfileViewModel by activityViewModels()
    private val viewModel: ReviewViewModel by activityViewModels()
    private var _binding: FragmentAccountReviewBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val createAccountRequest = CreateAccountRequest()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        generalViewModel.getAccountInfo().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        createAccountRequest.apply {
                            productId = it.accountId
                            currencyId = it.currencyId
                            initialAmount = it.initialAmount
                            accountTitle = it.accountTitle
                            mandateOfAccOperationId = it.operatingId
                            natureOfBusinessId = it.fundId
                        }
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        generalViewModel.getCustomerData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        createAccountRequest.apply {
                            val list = mutableListOf<JointCustomerInfo>()

                            for (value in it.customerData) {
                                list.add(JointCustomerInfo(1, value.customerId, value.isSignatory, value.isRequired))
                            }

                            jointCustomer = list
                        }
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        othersViewModel.getChequeBook().observe(viewLifecycleOwner, {
            createAccountRequest.isChequeBookEnable = it
        })

        othersViewModel.getSMSBanking().observe(viewLifecycleOwner, {
            createAccountRequest.isSmsBankingEnable = it
        })

        othersViewModel.getDebitCard().observe(viewLifecycleOwner, {
            createAccountRequest.isDebitCardEnable = it
        })

        othersViewModel.getEStatement().observe(viewLifecycleOwner, {
            createAccountRequest.isEStatementEnable = it
        })

        othersViewModel.getInternetBanking().observe(viewLifecycleOwner, {
            createAccountRequest.isInternetBankingEnable = it
        })

        nomineeViewModel.getSavedData().observe(viewLifecycleOwner, {
            GlobalScope.launch(Dispatchers.IO) {
                createAccountRequest.nominees = fileUriToBase64(it)
                viewModel.setDataPrepared(true)
            }
        })

        introducerViewModel.getIntroducerData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        createAccountRequest.introducerId = 0
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        introducerViewModel.getRelationId().observe(viewLifecycleOwner, {
            createAccountRequest.relationWithIntroducer = it
        })

        transactionViewModel.getTransactionProfile().observe(viewLifecycleOwner, {
            val list = mutableListOf<TransactionProfile>()

            for (details in it) {
                list.add(
                    TransactionProfile(
                        details.codeName,
                        details.limitDailyTrnAmt,
                        details.limitMaxTrnAmt,
                        details.limitMonthlyTrnAmt,
                        details.limitNoOfDailyTrn,
                        details.limitNoOfMonthlyTrn,
                        details.transactionProfileTypeId,
                        details.profileName
                    )
                )
            }

            createAccountRequest.transactionProfile = list
        })

        viewModel.getDataPrepared().observe(viewLifecycleOwner, {
            binding.btnNext.isEnabled = it
        })

        binding.btnNext.setOnClickListener {
            Log.e("Value", "" + createAccountRequest)
            viewModel.createAccount(createAccountRequest)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

        binding.btnAccountEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(0)
        }

        binding.btnOthersEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(1)
        }

        binding.btnNomineeEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(2)
        }

        binding.btnIntroducerEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(3)
        }

        binding.btnTransactionProfileEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

        viewModel.getCreateAccountData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

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

    fun fileUriToBase64(list: List<Nominee>): List<Nominee> {
        val nomineeList = mutableListOf<Nominee>()
        for (value in list) {

            if (value.photo.isNotEmpty() && Uri.parse(value.photo).isAbsolute) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            value.photo.toUri()
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        value.photo.toUri()
                    )
                }

                Base64Image.encode(bitmap) { base64 ->
                    base64?.let {
                        value.photo = it
                    }
                }
            }

            if (value.signaturePhoto.isNotEmpty() && Uri.parse(value.signaturePhoto).isAbsolute) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            value.signaturePhoto.toUri()
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        value.signaturePhoto.toUri()
                    )
                }

                Base64Image.encode(bitmap) { base64 ->
                    base64?.let {
                        value.signaturePhoto = it
                    }
                }
            }

            if (value.nidFrontPhoto.isNotEmpty() && Uri.parse(value.nidFrontPhoto).isAbsolute) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            value.nidFrontPhoto.toUri()
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        value.nidFrontPhoto.toUri()
                    )
                }

                Base64Image.encode(bitmap) { base64 ->
                    base64?.let {
                        value.nidFrontPhoto = it
                    }
                }
            }

            if (value.nidBackPhoto.isNotEmpty() && Uri.parse(value.nidBackPhoto).isAbsolute) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            value.nidBackPhoto.toUri()
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        value.nidBackPhoto.toUri()
                    )
                }

                Base64Image.encode(bitmap) { base64 ->
                    base64?.let {
                        value.nidBackPhoto = it
                    }
                }
            }

            nomineeList.add(value)
        }
        return nomineeList
    }
}