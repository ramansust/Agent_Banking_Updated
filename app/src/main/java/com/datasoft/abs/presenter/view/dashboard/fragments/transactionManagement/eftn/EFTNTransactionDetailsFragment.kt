package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.datasoft.abs.databinding.FragmentEftnTransactionDetailsBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EFTNTransactionDetailsFragment : Fragment() {

    private var _binding: FragmentEftnTransactionDetailsBinding? = null

    @Inject
    lateinit var accountAdapter: AccountAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEftnTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}