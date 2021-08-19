package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentTransactionBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.TransactionAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.deposit)
                1 -> tab.text = resources.getString(R.string.withdraw)
                2 -> tab.text = resources.getString(R.string.transfer)
                3 -> tab.text = resources.getString(R.string.balance_inquiry)
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}