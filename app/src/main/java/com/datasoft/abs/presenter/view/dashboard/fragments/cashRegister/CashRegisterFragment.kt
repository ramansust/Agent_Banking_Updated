package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.FragmentCashRegisterBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CashRegisterFragment : Fragment() {

    private val viewModel: CashRegisterViewModel by activityViewModels()
    private var _binding: FragmentCashRegisterBinding? = null

    @Inject
    lateinit var cashRegisterAdapter: CashRegisterListAdapter

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val list = mutableListOf<Row>()

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

        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_cash_register_to_cashRegisterCreateFragment)
        }

        binding.btnFeederTransaction.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_nav_cash_register_to_feederTransactionFragment)
        }

        viewModel.getCastRegisterData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        stopShimmer()
                        result.data?.let {
                            list.addAll(it.rows)
                            cashRegisterAdapter.differ.submitList(list)
                        }
                    }

                    Status.ERROR -> {
                        stopShimmer()
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }

                    Status.LOADING -> {
                        startShimmer()
                    }
                }

                showNoContent()
            }
        })

        viewModel.getSearchData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { search ->
                            cashRegisterAdapter.differ.submitList(list.filter {
                                it.senderAccNumber!!.contains(search, true)
                            })
                        }
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

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

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = cashRegisterAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun showNoContent() {
        binding.txtViewNoEntry.isVisible = list.size <= 0
        binding.recycleView.isVisible = list.size > 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}