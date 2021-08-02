package com.datasoft.abs.presenter.view.dashboard.fragments.accountList

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.accountList.AccountRequest
import com.datasoft.abs.databinding.FragmentAccountMainBinding
import com.datasoft.abs.presenter.utils.Status
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter.AccountMainAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerActivity
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
                0 -> tab.text = "Active"
                1 -> tab.text = "Awaiting"
                2 -> tab.text = "Draft"
//                3 -> tab.text = "Draft"
            }
        }.attach()

        viewModel.requestAccountData(
            AccountRequest(
                1,
                status = "${Status.ACTIVE.type}, ${Status.AWAITING.type}, ${Status.DRAFT.type}"
            )
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(), CustomerActivity::class.java))
        }

        binding.edTxtSearch.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            viewModel.setSearchData(charSequence.toString())
        }

        override fun afterTextChanged(editable: Editable) {}
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}