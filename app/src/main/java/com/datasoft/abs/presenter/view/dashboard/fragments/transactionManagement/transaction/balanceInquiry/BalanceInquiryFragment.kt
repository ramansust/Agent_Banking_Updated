package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.datasoft.abs.databinding.FragmentBalanceInquiryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceInquiryFragment : Fragment() {

    private var _binding: FragmentBalanceInquiryBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBalanceInquiryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}