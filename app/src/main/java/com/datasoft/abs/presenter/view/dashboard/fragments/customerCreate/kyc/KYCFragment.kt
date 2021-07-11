package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.KycFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KYCFragment : Fragment() {

    companion object {
        fun newInstance() = KYCFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: KYCViewModel by activityViewModels()
    private var _binding: KycFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(7)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}