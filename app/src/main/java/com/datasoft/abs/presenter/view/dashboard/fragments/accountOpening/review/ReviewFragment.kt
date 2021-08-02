package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentAccountReviewBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: ReviewViewModel by activityViewModels()
    private var _binding: FragmentAccountReviewBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {

        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

        binding.btnAccountEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(0)
        }

        binding.btnOthersEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(1)
        }

        binding.btnNomineeEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(2)
        }

        binding.btnIntroducerEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(3)
        }

        binding.btnTransactionProfileEdit.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}