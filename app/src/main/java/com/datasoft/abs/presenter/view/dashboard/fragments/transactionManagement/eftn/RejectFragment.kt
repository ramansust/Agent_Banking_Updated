package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.databinding.FragmentAwaitingApprovalBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RejectFragment : Fragment() {

    private var _binding: FragmentAwaitingApprovalBinding? = null
    private val viewModel: EFTNViewModel by activityViewModels()

    @Inject
    lateinit var rtgsAdapter: EFTNListAdapter

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
        startShimmer()

        val list = mutableListOf<Row>()
        viewModel.getSearchData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let { search ->
                        rtgsAdapter.differ.submitList(list.filter {
                            it.accountNumber.contains(search, true)
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
            val action =
                RejectFragmentDirections.actionRejectFragmentToEFTNTransactionDetailsFragment(
                    it.accountNumber
                )
            Navigation.findNavController(view).navigate(action)
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