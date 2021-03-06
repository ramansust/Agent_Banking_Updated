package com.datasoft.abs.presenter.view.dashboard.fragments.accountList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.databinding.FragmentAccountBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DraftFragment : Fragment() {

    private val viewModel: AccountMainViewModel by activityViewModels()
    private var _binding: FragmentAccountBinding? = null

    @Inject
    lateinit var accountAdapter: AccountAdapter

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
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initScrollListener()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getDraftData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    list.clear()
                    stopShimmer()

                    response.data?.let { customerResponse ->

                        list.addAll(customerResponse.rows)

                        accountAdapter.differ.submitList(list.map {
                            it.copy()
                        })

                        isLoading = list.size < customerResponse.pageNumber * Constant.PER_PAGE_ITEM
                    }
                }
                is Resource.Error -> {
                    stopShimmer()
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
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
                        accountAdapter.differ.submitList(list.filter {
                            it.accountTitle.contains(search, true) || it.accountNumber.contains(
                                search,
                                true
                            )
                        })
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        accountAdapter.setOnEditClickListener {
            toastHelper.sendToast("Clicked on EDIT")
        }
    }

    private fun initScrollListener() {
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
                        viewModel.loadMoreDraft()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}