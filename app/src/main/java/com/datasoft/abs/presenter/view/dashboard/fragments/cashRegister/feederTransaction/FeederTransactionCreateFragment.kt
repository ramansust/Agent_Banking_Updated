package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister.feederTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentFeederTransactionCreateBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FeederTransactionCreateFragment : Fragment() {

    private var _binding: FragmentFeederTransactionCreateBinding? = null
    private val viewModel: FeederTransactionViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeederTransactionCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            viewModel.createRequest(0, 0, 0, "", "", 0, "", "", "", 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}