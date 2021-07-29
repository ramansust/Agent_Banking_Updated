package com.datasoft.abs.presenter.view.dashboard.fragments.customerList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.customerList.Row
import com.datasoft.abs.databinding.FragmentCustomerBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Status
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.adapter.CustomerListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ActiveFragment : Fragment() {

    private val viewModel: CustomerListMainViewModel by activityViewModels()
    private var _binding: FragmentCustomerBinding? = null

    @Inject
    lateinit var customerAdapter: CustomerListAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val list = mutableListOf<Row>()

        viewModel.getAllCustomerData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    stopShimmer()
                    response.data?.let { customerResponse ->

                        list.addAll(customerResponse.rows.filter {
                            it.customerStatus == Status.ACTIVE.type
                        })

                        customerAdapter.differ.submitList(list)
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
            when(response) {
                is Resource.Success -> {
                    response.data?.let { search ->
                        customerAdapter.differ.submitList(list.filter {
                            it.fullName.contains(search, true)
                        })
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

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
            adapter = customerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}