package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.KycFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KYCFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: KYCViewModel by activityViewModels()
    private var _binding: KycFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var documentAdapter: VerifyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = KycFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        documentAdapter.differ.submitList(it.documentTypeList)
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

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        val onBoardingList = mutableListOf<CommonModel>()
        val residentList = mutableListOf<CommonModel>()
        val blackList = mutableListOf<CommonModel>()
        val pepList = mutableListOf<CommonModel>()
        val closeAssociatesList = mutableListOf<CommonModel>()
        val ipList = mutableListOf<CommonModel>()
        val productList = mutableListOf<CommonModel>()
        val professionList = mutableListOf<CommonModel>()
        val transactionList = mutableListOf<CommonModel>()
        val transparencyList = mutableListOf<CommonModel>()

        viewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        onBoardingList.addAll(it.typeOfOnboarding)
                        binding.spinnerOnBoarding.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, onBoardingList)

                        residentList.addAll(it.residentStatus)
                        binding.spinnerResidentStatus.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, residentList)

                        blackList.addAll(it.blackListed)
                        binding.spinnerBlackListed.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, blackList)

                        pepList.addAll(it.isPep)
                        binding.spinnerPoliticallyExposed.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, pepList)

                        closeAssociatesList.addAll(it.isPepCloser)
                        binding.spinnerCloseAssociates.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, closeAssociatesList)

                        ipList.addAll(it.isInterviewedPersonally)
                        binding.spinnerInterviewedPersonally.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ipList)

                        productList.addAll(it.typeOfProduct)
                        binding.spinnerTypeProduct.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productList)

                        professionList.addAll(it.profession)
                        binding.spinnerProfession.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productList)

                        transactionList.addAll(it.transactionalRisk)
                        binding.spinnerYearlyTransaction.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transactionList)

                        transparencyList.addAll(it.transparencyRisk)
                        binding.spinnerTransparencyRisk.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transparencyList)
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

        viewModel.getKYCData().observe(viewLifecycleOwner, {
            if(onBoardingList.isNotEmpty()) binding.spinnerOnBoarding.setSelection(onBoardingList.indexOf(
                CommonModel(it.typeOfOnboarding)
            ))

            if(residentList.isNotEmpty()) binding.spinnerResidentStatus.setSelection(residentList.indexOf(
                CommonModel(it.residentStatus)
            ))

            if(blackList.isNotEmpty()) binding.spinnerBlackListed.setSelection(blackList.indexOf(
                CommonModel(it.blackListed)
            ))

            if(pepList.isNotEmpty()) binding.spinnerPoliticallyExposed.setSelection(pepList.indexOf(
                CommonModel(it.isPep)
            ))

            if(closeAssociatesList.isNotEmpty()) binding.spinnerCloseAssociates.setSelection(closeAssociatesList.indexOf(
                CommonModel(it.isPepCloser)
            ))

            if(ipList.isNotEmpty()) binding.spinnerInterviewedPersonally.setSelection(ipList.indexOf(
                CommonModel(it.isInterviewedPersonally)
            ))

            if(productList.isNotEmpty()) binding.spinnerTypeProduct.setSelection(productList.indexOf(
                CommonModel(it.typeOfProduct)
            ))

            if(professionList.isNotEmpty()) binding.spinnerProfession.setSelection(professionList.indexOf(
                CommonModel(it.profession)
            ))

            if(transactionList.isNotEmpty()) binding.spinnerYearlyTransaction.setSelection(transactionList.indexOf(
                CommonModel(it.transactionalRisk)
            ))

            if(transparencyList.isNotEmpty()) binding.spinnerTransparencyRisk.setSelection(transparencyList.indexOf(
                CommonModel(it.transparencyRisk)
            ))
        })

        binding.btnNext.setOnClickListener {
            viewModel.saveData(
                if(onBoardingList.isNotEmpty()) onBoardingList[binding.spinnerOnBoarding.selectedItemPosition].id else 0,
                if(residentList.isNotEmpty()) residentList[binding.spinnerResidentStatus.selectedItemPosition].id else 0,
                if(blackList.isNotEmpty()) blackList[binding.spinnerBlackListed.selectedItemPosition].id else 0,
                if(pepList.isNotEmpty()) pepList[binding.spinnerPoliticallyExposed.selectedItemPosition].id else 0,
                if(closeAssociatesList.isNotEmpty()) closeAssociatesList[binding.spinnerCloseAssociates.selectedItemPosition].id else 0,
                if(ipList.isNotEmpty()) ipList[binding.spinnerInterviewedPersonally.selectedItemPosition].id else 0,
                if(productList.isNotEmpty()) productList[binding.spinnerTypeProduct.selectedItemPosition].id else 0,
                if(professionList.isNotEmpty()) professionList[binding.spinnerProfession.selectedItemPosition].id else 0,
                if(transactionList.isNotEmpty()) transactionList[binding.spinnerYearlyTransaction.selectedItemPosition].id else 0,
                if(transparencyList.isNotEmpty()) transparencyList[binding.spinnerTransparencyRisk.selectedItemPosition].id else 0,
            )
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}