package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.FragmentTransactionWithdrawBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawFragment : Fragment() {

    private var _binding: FragmentTransactionWithdrawBinding? = null
    private val viewModel: TransactionViewModel by activityViewModels()
    private val withdrawViewModel: WithdrawViewModel by activityViewModels()

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var acType = ""
    private var accountNumber = ""
    private var branchID = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionWithdrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paymentList = mutableListOf<CommonModel>()
        val currencyList = mutableListOf<CommonModel>()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getAccountDetails().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let {
                        acType = it.acType!!
                        accountNumber = it.accountNo!!
                        branchID = it.branchId!!

                        paymentList.addAll(it.trnProfileType!!)
                        binding.spinnerPaymentMode.adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            paymentList
                        )

                        currencyList.addAll(it.currencyData!!)
                        binding.spinnerCurrency.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                currencyList
                            )
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                is Resource.Loading -> {

                }

            }
        })

        withdrawViewModel.getAmountDetails().observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.edTxtChargeVat.setText(it.chargeAmt)
                        binding.edTxtInWords.setText(it.currencyToWord)
                        binding.edTxtTotal.setText(it.transactionalAmount)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                is Resource.Loading -> {

                }
            }
        })

        binding.edTxtAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.edTxtAmount.text.trim().isNotEmpty()) {
                withdrawViewModel.amountDetails(
                    acType, accountNumber, branchID, 1,
                    if (binding.edTxtAmount.text.trim().toString()
                            .isNotEmpty()
                    ) binding.edTxtAmount.text.trim().toString()
                        .toInt() else 0,
                    if (currencyList.isNotEmpty()) currencyList[binding.spinnerCurrency.selectedItemPosition].id else 0,
                    if (paymentList.isNotEmpty()) paymentList[binding.spinnerPaymentMode.selectedItemPosition].id else 0,
                    2
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}