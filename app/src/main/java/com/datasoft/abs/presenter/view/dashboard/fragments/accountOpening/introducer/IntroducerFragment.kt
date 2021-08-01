package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.IntroducerFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroducerFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: IntroducerViewModel by activityViewModels()
    private var _binding: IntroducerFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = IntroducerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}