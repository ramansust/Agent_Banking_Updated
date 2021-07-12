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
    lateinit var documentAdapter: DocumentListAdapter

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

        viewModel.configData()
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

        viewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        binding.spinnerOnBoarding.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.typeOfOnboarding)

                        binding.spinnerResidentStatus.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.residentStatus)

                        binding.spinnerBlackListed.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.blackListed)

                        binding.spinnerPoliticallyExposed.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.isPep)

                        binding.spinnerCloseAssociates.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.isPepCloser)

                        binding.spinnerInterviewedPersonally.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.isInterviewedPersonally)

                        binding.spinnerTypeProduct.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.typeOfProduct)

                        binding.spinnerProfession.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.profession)

                        binding.spinnerYearlyTransaction.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.transactionalRisk)

                        binding.spinnerTransparencyRisk.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it.transparencyRisk)
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

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(7)
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