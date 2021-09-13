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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountRequest
import com.datasoft.abs.data.dto.createAccount.review.JointCustomerInfo
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.data.dto.createAccount.review.TransactionProfile
import com.datasoft.abs.databinding.FragmentAccountReviewBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general.GeneralViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer.IntroducerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee.NomineeViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others.OthersViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile.TransactionProfileViewModel
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val othersList = mutableListOf<Int>()

    @Inject
    lateinit var nomineeAdapter: NomineeAdapter

    @Inject
    lateinit var transactionAdapter: TransactionAdapter

    @Inject
    lateinit var toastHelper: ToastHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        setupRecyclerView()
        setupRecyclerViewTransaction()

        generalViewModel.getAccountInfo().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {
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

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

        generalViewModel.getDisplayAccountInfo().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        binding.txtViewAccountValue.text = it.accountTitle
                        binding.txtViewProductValue.text = it.category
                        binding.txtViewAccountTypeValue.text = it.account
                        binding.txtViewOperatingValue.text = it.operating
                        binding.txtViewOpeningValue.text = it.openingDate
                        binding.txtViewCurrencyValue.text = it.currency
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        })

        generalViewModel.getCustomerData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        createAccountRequest.apply {
                            val list = mutableListOf<JointCustomerInfo>()

                            for (value in it.customerData) {
                                list.add(
                                    JointCustomerInfo(
                                        1,
                                        value.customerId,
                                        value.isSignatory,
                                        value.isRequired
                                    )
                                )
                            }

                            jointCustomer = list
                        }
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        })

        othersViewModel.getChequeBook().observe(viewLifecycleOwner, {
            createAccountRequest.isChequeBookEnable = it
            if (it) othersList.add(0)
        })

        othersViewModel.getSMSBanking().observe(viewLifecycleOwner, {
            createAccountRequest.isSmsBankingEnable = it
            if (it) othersList.add(1)
        })

        othersViewModel.getDebitCard().observe(viewLifecycleOwner, {
            createAccountRequest.isDebitCardEnable = it
            if (it) othersList.add(2)
        })

        othersViewModel.getEStatement().observe(viewLifecycleOwner, {
            createAccountRequest.isEStatementEnable = it
            if (it) othersList.add(3)
        })

        othersViewModel.getInternetBanking().observe(viewLifecycleOwner, {
            createAccountRequest.isInternetBankingEnable = it
            if (it) othersList.add(4)
        })

        othersViewModel.getNotifyData().observe(viewLifecycleOwner, {
            otherFacilities()
        })

        nomineeViewModel.getSavedData().observe(viewLifecycleOwner, {
            nomineeAdapter.differ.submitList(it)

            lifecycleScope.launch(Dispatchers.IO) {
                createAccountRequest.nominees = fileUriToBase64(it)
                viewModel.setDataPrepared(true)
            }
        })

        introducerViewModel.getIntroducerData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {
                            createAccountRequest.introducerId = 0
                            binding.txtViewNameValue.text = it.fullName
                            binding.txtViewAccountNumberValue.text = it.accountNumber
                        }
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

        introducerViewModel.getRelationId().observe(viewLifecycleOwner, {
            createAccountRequest.relationWithIntroducer = it
        })

        transactionViewModel.getTransactionProfile().observe(viewLifecycleOwner, {
            transactionAdapter.differ.submitList(it)

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

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        goneProgressBar()
                        result.data?.let {
                            toastHelper.sendToast(it.message)
                        }
                    }

                    Status.ERROR -> {
                        goneProgressBar()
                        result.message?.let { message ->
                            Log.e("TAG", "An error occurred: $message")
                        }
                    }

                    Status.LOADING -> {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun otherFacilities() {
        val interested = StringBuilder()
        val notInterested = StringBuilder()
        if (othersList.isNotEmpty()) {
            for (i in 0..4) {
                if (othersList.indexOf(i) >= 0) {
                    when (i) {
                        0 -> interested.append(resources.getString(R.string.cheque_book))
                            .append(", ")
                        1 -> interested.append(resources.getString(R.string.sms_banking))
                            .append(", ")
                        2 -> interested.append(resources.getString(R.string.debit_card))
                            .append(", ")
                        3 -> interested.append(resources.getString(R.string.e_statement))
                            .append(", ")
                        4 -> interested.append(resources.getString(R.string.internet_banking))
                            .append(", ")
                    }
                } else {
                    when (i) {
                        0 -> notInterested.append(resources.getString(R.string.cheque_book))
                            .append(", ")
                        1 -> notInterested.append(resources.getString(R.string.sms_banking))
                            .append(", ")
                        2 -> notInterested.append(resources.getString(R.string.debit_card))
                            .append(", ")
                        3 -> notInterested.append(resources.getString(R.string.e_statement))
                            .append(", ")
                        4 -> notInterested.append(resources.getString(R.string.internet_banking))
                            .append(" ,")
                    }
                }
            }

            if (interested.isNotEmpty() && interested.length > 2)
                binding.txtViewInterestedServiceValue.text =
                    interested.substring(0, interested.length - 2)
            if (notInterested.isNotEmpty() && notInterested.length > 2)
                binding.txtViewNotInterestedValue.text =
                    notInterested.substring(0, notInterested.length - 2)

        } else {
            val notInterestedValue =
                resources.getString(R.string.cheque_book) + ", " + resources.getString(R.string.sms_banking) + ", " + resources.getString(
                    R.string.debit_card
                ) + ", " + resources.getString(R.string.e_statement) + ", " + resources.getString(R.string.internet_banking)
            binding.txtViewNotInterestedValue.text = notInterestedValue
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

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = nomineeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupRecyclerViewTransaction() {
        binding.recyclerViewTransaction.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}