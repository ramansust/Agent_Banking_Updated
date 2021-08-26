package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.databinding.FragmentAwaitingApprovalBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DisburseFragment : Fragment() {

    private var _binding: FragmentAwaitingApprovalBinding? = null
    private val viewModel: RTGSViewModel by activityViewModels()

    @Inject
    lateinit var rtgsAdapter: RTGSListAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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

        val list = mutableListOf<com.datasoft.abs.data.dto.transaction.rtgs.Row>()
        viewModel.getRTGSData().observe(viewLifecycleOwner, { response ->

//            list.clear()

            when (response) {
                is Resource.Success -> {
                    stopShimmer()
                    response.data?.let {
                        list.addAll(it.rows)
                        rtgsAdapter.differ.submitList(list)
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
                        rtgsAdapter.differ.submitList(list.filter {
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

        rtgsAdapter.setOnItemClickListener {
            viewModel.setDetails(it.id!!)
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

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}