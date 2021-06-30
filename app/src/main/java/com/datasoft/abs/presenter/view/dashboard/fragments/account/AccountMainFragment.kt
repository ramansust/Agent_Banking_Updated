package com.datasoft.abs.presenter.view.dashboard.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentAccountMainBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.account.adapter.AccountMainAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountMainFragment : Fragment() {

    private val viewModel: AccountMainViewModel by activityViewModels()
    private var _binding: FragmentAccountMainBinding? = null

    @Inject
    lateinit var adapter: AccountMainAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when(position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Active"
                2 -> tab.text = "Awaiting"
                3 -> tab.text = "Draft"
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}