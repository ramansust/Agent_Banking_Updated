package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister.feederTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.FragmentFeederTransactionBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeederTransactionFragment : Fragment() {

    private val viewModel: FeederTransactionViewModel by activityViewModels()
    private var _binding: FragmentFeederTransactionBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var feederTranListAdapter: FeederTransactionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeederTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_feederTransactionFragment_to_feederTransactionCreateFragment)
        }

        val list = mutableListOf<Row>()
        viewModel.getFeederData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    stopShimmer()
                    response.data?.let {
                        list.addAll(it.rows)
                        feederTranListAdapter.differ.submitList(list)
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
                        feederTranListAdapter.differ.submitList(list.filter {
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

        binding.edTxtSearch.setOnQueryTextListener(textQuery)
    }

    private val textQuery = object : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            viewModel.setSearchData(newText)
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }
    }

    private fun setupRecyclerView() {
        binding.recycleView.apply {
            adapter = feederTranListAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startShimmer() {
        binding.shimmerView.startShimmer()
    }

    private fun stopShimmer() {
        binding.shimmerView.stopShimmer()
        binding.shimmerView.visibility = View.GONE
    }
}