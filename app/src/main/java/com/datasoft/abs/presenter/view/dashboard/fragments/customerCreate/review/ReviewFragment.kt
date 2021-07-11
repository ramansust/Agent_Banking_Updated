package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FingerprintFragmentBinding
import com.datasoft.abs.databinding.FragmentReviewBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    companion object {
        fun newInstance() = ReviewFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: ReviewViewModel by activityViewModels()
    private var _binding: FragmentReviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        binding.btnNext.setOnClickListener {
//            customerViewModel.requestCurrentStep(6)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(6)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}