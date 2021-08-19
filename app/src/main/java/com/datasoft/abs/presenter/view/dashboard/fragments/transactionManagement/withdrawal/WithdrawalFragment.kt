package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.withdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.CommonRequest
import com.datasoft.abs.databinding.FragmentWithdrawBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit.DepositAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawalFragment : Fragment() {

    private val viewModel: WithdrawalViewModel by activityViewModels()
    private var _binding: FragmentWithdrawBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var depositAdapter: DepositAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        viewModel.requestWithdrawData(CommonRequest(1))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getWithdrawData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let { dataResponse ->
                        depositAdapter.differ.submitList(dataResponse.rows)
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
            adapter = depositAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}