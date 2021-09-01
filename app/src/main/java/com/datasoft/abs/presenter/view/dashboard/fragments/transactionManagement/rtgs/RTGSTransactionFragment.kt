package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.FragmentRtgsTransactionBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RTGSTransactionFragment : Fragment() {

    private var _binding: FragmentRtgsTransactionBinding? = null
    private val viewModel: RTGSTransactionViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var senderAccountNo = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRtgsTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestBankList()

        val bankList = mutableListOf<CommonModel>()
        val branchList = mutableListOf<CommonModel>()

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
                    }
                }

                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.btnNext.isEnabled = false
                        findNavController().navigateUp()
                    }
                }

                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                if (binding.edTxtCharge.text.trim().toString()
                        .isNotEmpty()
                ) binding.edTxtCharge.text.trim().toString()
                    .toInt() else 0,
                binding.edTxtReceiverAccountNo.text.trim().toString(),
                binding.edTxtReceiverAddress.text.trim().toString(),
                if (branchList.isNotEmpty()) branchList[binding.spinnerBank.selectedItemPosition].id else 0,
                binding.edTxtReceiverCity.text.trim().toString(),
                binding.edTxtReceiverName.text.trim().toString(),
                binding.edTxtSenderAccountNo.text.trim().toString(),
                if (binding.edTxtVat.text.trim().toString()
                        .isNotEmpty()
                ) binding.edTxtVat.text.trim().toString()
                    .toInt() else 0,
            )
        }
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