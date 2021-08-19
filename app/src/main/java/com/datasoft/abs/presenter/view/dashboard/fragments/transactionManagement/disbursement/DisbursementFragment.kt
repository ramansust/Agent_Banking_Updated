package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.disbursement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.datasoft.abs.databinding.FragmentDisbursementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisbursementFragment : Fragment() {

    private var _binding: FragmentDisbursementBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDisbursementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}