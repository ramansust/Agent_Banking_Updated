package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.fingerprint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FingerprintFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FingerprintFragment : Fragment() {

    companion object {
        fun newInstance() = FingerprintFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: FingerprintViewModel by activityViewModels()
    private var _binding: FingerprintFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FingerprintFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}