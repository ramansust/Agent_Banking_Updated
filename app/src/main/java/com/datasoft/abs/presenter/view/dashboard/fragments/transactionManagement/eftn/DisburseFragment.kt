package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.FragmentAwaitingApprovalBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DisburseFragment : Fragment() {

    private var _binding: FragmentAwaitingApprovalBinding? = null
    private val viewModel: EFTNViewModel by activityViewModels()
    private val detailsViewModel: EFTNTransactionViewModel by activityViewModels()

    @Inject
    lateinit var eftnAdapter: EFTNListAdapter

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

        viewModel.getDisburseData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    list.clear()
                    stopShimmer()

                    response.data?.let { dataResponse ->
                        list.addAll(dataResponse.rows)

                        eftnAdapter.differ.submitList(list.map {
                            it.copy()
                        })

                        isLoading = list.size < dataResponse.pageNumber * Constant.PER_PAGE_ITEM
                    }
                }

                is Resource.Error -> {
                    stopShimmer()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    startShimmer()
                }
            }
        })

        viewModel.getSearchData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let { search ->
                        eftnAdapter.differ.submitList(list.filter {
                            it.senderAccNumber!!.contains(search, true)
                        })
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        eftnAdapter.setOnItemClickListener {
            detailsViewModel.setDetails(it.id!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = eftnAdapter
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

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}