package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.databinding.FragmentTransactionDetailsBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TransactionDetailsFragment : Fragment() {

    private val viewModel: TransactionDetailsViewModel by viewModels()
    private var _binding: FragmentTransactionDetailsBinding? = null

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var transactionDetailsAdapter: TransactionDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getTransactionDetails().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    stopShimmer()

                    response.data?.let { dataResponse ->

                        binding.txtViewTransactionNoValue.text = dataResponse[0].transactionNo
                        binding.txtViewTransactionTypeValue.text = dataResponse[0].transactionType

                        try {
                            binding.txtViewDateValue.text = SimpleDateFormat(
                                Constant.DATE_FORMAT,
                                Locale.US
                            ).format(
                                SimpleDateFormat(Constant.DATE_FORMAT_API, Locale.US).parse(
                                    dataResponse[0].transactionDate
                                )
                            )
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        transactionDetailsAdapter.differ.submitList(dataResponse)
                    }
                }

                is Resource.Loading -> {
                    startShimmer()
                }

                is Resource.Error -> {
                    stopShimmer()
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = transactionDetailsAdapter
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