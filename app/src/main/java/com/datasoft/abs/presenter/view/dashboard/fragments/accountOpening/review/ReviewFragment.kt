package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.createAccount.review.CreateAccountRequest
import com.datasoft.abs.data.dto.createAccount.review.TransactionProfile
import com.datasoft.abs.databinding.FragmentAccountReviewBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general.GeneralViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer.IntroducerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee.NomineeViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others.OthersViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile.TransactionProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        generalViewModel.getAccountInfo().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        createAccountRequest.apply {
                            productId = it.categoryId
                            customerId = it.accountId
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
            createAccountRequest.nominees = it
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

        binding.btnNext.setOnClickListener {
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
}