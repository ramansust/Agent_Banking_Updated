package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.FragmentTransactionDepositBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFragment : Fragment() {

    private var _binding: FragmentTransactionDepositBinding? = null
    private val viewModel: TransactionViewModel by activityViewModels()
    private val depositViewModel: DepositViewModel by activityViewModels()

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

        _binding = FragmentTransactionDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paymentList = mutableListOf<CommonModel>()
        val currencyList = mutableListOf<CommonModel>()

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
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {

                }

            }
        })

        depositViewModel.getAmountDetails().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.edTxtChargeVat.setText(it.chargeAmt)
                        binding.edTxtInWords.setText(it.currencyToWord)
                        binding.edTxtTotal.setText(it.transactionalAmount)
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

        binding.edTxtAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.edTxtAmount.text.trim().isNotEmpty()) {
                depositViewModel.amountDetails(
                    acType, accountNumber, branchID, 1,
                    if (binding.edTxtAmount.text.trim().toString()
                            .isNotEmpty()
                    ) binding.edTxtAmount.text.trim().toString()
                        .toInt() else 0,
                    if (currencyList.isNotEmpty()) currencyList[binding.spinnerCurrency.selectedItemPosition].id else 0,
                    if (paymentList.isNotEmpty()) paymentList[binding.spinnerPaymentMode.selectedItemPosition].id else 0,
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