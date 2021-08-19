package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentRtgsBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn.EFTNAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RTGSFragment : Fragment() {

    private var _binding: FragmentRtgsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRtgsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pager.adapter = EFTNAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.awaiting_approval)
                1 -> tab.text = resources.getString(R.string.disburse)
                2 -> tab.text = resources.getString(R.string.reject)
            }
        }.attach()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_rtgs_to_RTGSTransactionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}