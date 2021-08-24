package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentCashRegisterBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CashRegisterFragment : Fragment() {

    private val viewModel: CashRegisterViewModel by activityViewModels()
    private var _binding: FragmentCashRegisterBinding? = null

    @Inject
    lateinit var cashRegisterAdapter: CashRegisterListAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCashRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_cash_register_to_cashRegisterCreateFragment)
        }

        binding.btnFeederTransaction.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_cash_register_to_feederTransactionFragment)
        }

        viewModel.getSearchData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { search ->
//                        cashRegisterAdapter.differ.submitList(list.filter {
//                            it.accountNumber.contains(search, true)
//                        })
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        binding.edTxtSearch.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            viewModel.setSearchData(charSequence.toString())
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = cashRegisterAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}