package com.datasoft.abs.presenter.view.dashboard.fragments.accountList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentAccountMainBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter.AccountMainAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountMainFragment : Fragment() {

    private val viewModel: AccountMainViewModel by activityViewModels()
    private var _binding: FragmentAccountMainBinding? = null

    @Inject
    lateinit var adapter: AccountMainAdapter

    // This property is only valid between onCreateView and onDestroyView.
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
            when (position) {
                0 -> tab.text = resources.getString(R.string.active)
                1 -> tab.text = resources.getString(R.string.awaiting)
                2 -> tab.text = resources.getString(R.string.draft)
            }
        }.attach()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AccountActivity::class.java))
        }

        binding.edTxtSearch.setOnQueryTextListener(textQuery)
    }

    private val textQuery = object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            viewModel.setSearchData(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}