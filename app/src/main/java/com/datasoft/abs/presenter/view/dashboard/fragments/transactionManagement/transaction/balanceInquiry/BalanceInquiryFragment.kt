package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.databinding.FragmentBalanceInquiryBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BalanceInquiryFragment : Fragment() {

    private var _binding: FragmentBalanceInquiryBinding? = null
    private val viewModel: TransactionViewModel by activityViewModels()
    private val balanceInquiryViewModel: BalanceInquiryViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var balanceInquiryAdapter: BalanceInquiryAdapter

    @Inject
    lateinit var toastHelper: ToastHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBalanceInquiryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        balanceInquiryViewModel.getBalanceInquiry().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        stopShimmer()
                        result.data?.let { dataResponse ->
                            balanceInquiryAdapter.differ.submitList(dataResponse)
                        }
                    }

                    Status.LOADING -> {
                        startShimmer()
                    }

                    Status.ERROR -> {
                        stopShimmer()
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }
                }
            }
        })

        viewModel.getAccountNumber().observe(viewLifecycleOwner, {
            if (it.isNotEmpty())
                balanceInquiryViewModel.balanceInquiry(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = balanceInquiryAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}