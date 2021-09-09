package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.FragmentEftnTransactionBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EFTNTransactionFragment : Fragment() {

    private var _binding: FragmentEftnTransactionBinding? = null
    private val viewModel: EFTNTransactionViewModel by viewModels()

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var senderAccountNo = ""
    private var currentBalance = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEftnTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bankList = mutableListOf<CommonModel>()
        val branchList = mutableListOf<CommonModel>()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getBankList().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        bankList.addAll(it)
                        binding.spinnerBank.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                bankList
                            )
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

        viewModel.getBranchList().observe(viewLifecycleOwner, { response ->
            branchList.clear()

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        branchList.addAll(it)
                        binding.spinnerBranch.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                branchList
                            )
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

        binding.spinnerBank.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.requestBranchList(bankList[position].id)
                }
            }

        viewModel.getAccountDetails().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let {
                        senderAccountNo = it.accountNo!!
                        binding.btnNext.isEnabled = true
                        binding.btnAgentFinger.isEnabled = true
                        binding.btnCustomerFinger.isEnabled = true
                        currentBalance = it.balance!!
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

        viewModel.getCreationData().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let {
                        binding.btnNext.isEnabled = false
                        findNavController().navigateUp()
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

        binding.imgViewSearch.setOnClickListener {
            viewModel.accountDetails(binding.edTxtSenderAccountNo.text.trim().toString())
        }

        binding.btnNext.setOnClickListener {
            viewModel.createRequest(
                if (binding.edTxtAmount.text.trim().toString()
                        .isNotEmpty()
                ) binding.edTxtAmount.text.trim().toString()
                    .toInt() else 0,
                if (bankList.isNotEmpty()) bankList[binding.spinnerBank.selectedItemPosition].id else 0,
                binding.edTxtReceiverAccountNo.text.trim().toString(),
                if (branchList.isNotEmpty()) branchList[binding.spinnerBank.selectedItemPosition].id else 0,
                binding.edTxtReceiverEmail.text.trim().toString(),
                binding.edTxtReceiverMobile.text.trim().toString(),
                binding.edTxtReceiverName.text.trim().toString(),
                binding.edTxtRemarks.text.trim().toString(),
                binding.edTxtSenderAccountNo.text.trim().toString(),
            )
        }

        binding.edTxtAmount.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            if(charSequence.toString().isNotEmpty() && charSequence.toString().toInt() > currentBalance) {
                binding.edTxtAmount.setText(resources.getString(R.string.zero))
                toastHelper.sendToast(resources.getString(R.string.balance_exceed))
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }
}