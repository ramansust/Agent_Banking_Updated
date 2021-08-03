package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.config.TpDetail
import com.datasoft.abs.databinding.TransactionProfileFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionProfileFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: TransactionProfileViewModel by activityViewModels()
    private var _binding: TransactionProfileFragmentBinding? = null

    @Inject
    lateinit var transactionAdapter: TransactionProfileAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = TransactionProfileFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(5)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(3)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        setupRecyclerView()

        accountViewModel.getTransactionProfileData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        val list = mutableListOf<TpDetail>()

                        for (details in it.tpDetails) {
                            details.name =
                                it.transactionProfileList[it.transactionProfileList.indexOf(
                                    CommonModel(details.transactionProfileName)
                                )].name + " " + it.transactionCodeList[it.transactionCodeList.indexOf(
                                    CommonModel(details.code)
                                )].name
                            list.add(details)
                        }

                        transactionAdapter.differ.submitList(list)

                        binding.txtViewNoEntry.isVisible = it.tpDetails.isEmpty()
                        binding.recyclerView.isVisible = it.tpDetails.isNotEmpty()
                        binding.linearLayoutHeader.isVisible = it.tpDetails.isNotEmpty()

                        binding.btnNext.isEnabled = it.tpDetails.isNotEmpty()
                    }
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//            (this.layoutManager as LinearLayoutManager).stackFromEnd = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}