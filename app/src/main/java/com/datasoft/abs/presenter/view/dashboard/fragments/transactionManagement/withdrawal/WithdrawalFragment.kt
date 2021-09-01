package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.withdrawal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.Row
import com.datasoft.abs.databinding.FragmentWithdrawBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit.DepositAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawalFragment : Fragment() {

    private val viewModel: WithdrawalViewModel by activityViewModels()
    private var _binding: FragmentWithdrawBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    val list = mutableListOf<Row>()
    private var isLoading = false

    @Inject
    lateinit var depositAdapter: DepositAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initScrollListener()

        viewModel.getWithdrawData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    list.clear()
                    stopShimmer()

                    response.data?.let { dataResponse ->
                        dataResponse.rows?.let { list.addAll(it) }

                        depositAdapter.differ.submitList(list.map {
                            it.copy()
                        })

                        isLoading = list.size < dataResponse.pageNumber * Constant.PER_PAGE_ITEM
                    }
                }

                is Resource.Loading -> {
                    startShimmer()
                }

                is Resource.Error -> {
                    stopShimmer()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.getSearchData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { search ->
                        depositAdapter.differ.submitList(list.filter {
                            it.drAccountNumber!!.contains(search, true)
                        })
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })

        depositAdapter.setOnItemClickListener {
            val action =
                WithdrawalFragmentDirections.actionWithdrawToTransactionDetailsFragment(it.transactionNo!!)
            Navigation.findNavController(view).navigate(action)
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

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = depositAdapter
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
                        viewModel.loadMore()
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