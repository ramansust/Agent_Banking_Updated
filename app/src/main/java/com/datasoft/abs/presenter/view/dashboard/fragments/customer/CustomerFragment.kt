package com.datasoft.abs.presenter.view.dashboard.fragments.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.customer.CustomerRequest
import com.datasoft.abs.databinding.FragmentCustomerBinding
import com.datasoft.abs.presenter.utils.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.customer.adapter.CustomerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomerFragment : Fragment() {

    private val viewModel: CustomerViewModel by viewModels()
    private var _binding: FragmentCustomerBinding? = null

    @Inject
    lateinit var customerAdapter: CustomerAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        viewModel.getCustomerData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { customerResponse ->
                        customerAdapter.differ.submitList(customerResponse.rows)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })

        viewModel.requestCustomerData(CustomerRequest(1, status = "1"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = customerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}