package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentTransactionTransferBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransferFragment : Fragment() {

    private var _binding: FragmentTransactionTransferBinding? = null
    private val viewModel: TransferViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

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

        _binding = FragmentTransactionTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        transactionViewModel.getAccountDetails().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        acType = it.acType!!
                        accountNumber = it.accountNo!!
                        branchID = it.branchId!!
                    }
                }

                Status.ERROR -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                Status.LOADING -> {

                }
            }
        })

        viewModel.getReceiverDetails().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        binding.edTxtAccountTitle.setText(it.toAccountTitle)
                    }
                }

                Status.ERROR -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                Status.LOADING -> {

                }
            }

        })

        viewModel.getAmountDetails().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        try {
                            binding.edTxtChargeVat.setText(
                                ((it.chargeAmt?.toDouble() ?: 0.0) + (it.vatAmt?.toDouble()
                                    ?: 0.0)).toString()
                            )
                            binding.edTxtAmount.setText(
                                ((it.chargeAmt?.toDouble() ?: 0.0) + (it.vatAmt?.toDouble()
                                    ?: 0.0) + (it.transactionalAmount?.toDouble()
                                    ?: 0.0)).toString()
                            )
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        }
                        binding.edTxtInWords.setText(it.currencyToWord)
                        binding.edTxtTotal.setText(it.transactionalAmount)
                    }
                }

                Status.ERROR -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                Status.LOADING -> {

                }
            }
        })

        binding.imgViewSearch.setOnClickListener {
            viewModel.receiverDetails(binding.edTxtReceiverAccountNo.text.trim().toString())
        }

        binding.edTxtAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.edTxtAmount.text.trim().isNotEmpty()) {
                viewModel.amountDetails(
                    acType, accountNumber, branchID, 1,
                    if (binding.edTxtAmount.text.trim().toString()
                            .isNotEmpty()
                    ) binding.edTxtAmount.text.trim().toString()
                        .toInt() else 0,
                    1,
                    1,
                    1
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}