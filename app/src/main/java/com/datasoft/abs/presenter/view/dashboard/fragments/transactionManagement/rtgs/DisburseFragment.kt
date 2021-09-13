package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.FragmentAwaitingApprovalBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn.EFTNTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DisburseFragment : Fragment() {

    private var _binding: FragmentAwaitingApprovalBinding? = null
    private val viewModel: RTGSViewModel by viewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val detailsViewModel: EFTNTransactionViewModel by activityViewModels()

    @Inject
    lateinit var rtgsAdapter: RTGSListAdapter

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val list = mutableListOf<Row>()
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAwaitingApprovalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initScrollListener()

        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        viewModel.getDisburseData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    list.clear()
                    stopShimmer()

                    response.data?.let { dataResponse ->
                        list.addAll(dataResponse.rows)

                        rtgsAdapter.differ.submitList(list.map {
                            it.copy()
                        })

                        isLoading = list.size < dataResponse.pageNumber * Constant.PER_PAGE_ITEM

                        showNoContent()
                    }
                }

                Status.ERROR -> {
                    stopShimmer()
                    response.message?.let { message ->
                        showNoContent()
                        toastHelper.sendToast(message)
                    }
                }

                Status.LOADING -> {
                    startShimmer()
                }
            }
        })

        searchViewModel.getSearchData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let { search ->
                        rtgsAdapter.differ.submitList(list.filter {
                            it.senderAccNumber!!.contains(
                                search,
                                true
                            ) || it.receiverName!!.contains(
                                search,
                                true
                            ) || it.receiverAccNumber!!.contains(search, true)
                        })
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        })

        rtgsAdapter.setOnItemClickListener {
            detailsViewModel.setDetails(it.id!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = rtgsAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initScrollListener() {
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                        viewModel.loadMoreDisburse()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun showNoContent() {
        binding.txtViewNoEntry.isVisible = list.size <= 0
        binding.recycleView.isVisible = list.size > 0
    }

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}