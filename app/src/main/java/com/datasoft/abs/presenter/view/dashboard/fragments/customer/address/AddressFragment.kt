package com.datasoft.abs.presenter.view.dashboard.fragments.customer.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.AddressFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customer.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressFragment : Fragment() {

    companion object {
        fun newInstance() = AddressFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: AddressViewModel by activityViewModels()
    private var _binding: AddressFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AddressFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}