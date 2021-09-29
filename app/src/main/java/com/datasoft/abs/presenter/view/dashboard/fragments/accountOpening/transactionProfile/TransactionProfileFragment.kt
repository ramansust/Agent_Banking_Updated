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
import com.datasoft.abs.presenter.states.Status
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

        val list = mutableListOf<TpDetail>()

        accountViewModel.getTransactionProfileData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {

                        for (details in it.tpDetails) {
                            details.profileName =
                                it.transactionProfileList[it.transactionProfileList.indexOf(
                                    CommonModel(details.transactionProfileName)
                                )].name
                            details.codeName =
                                it.transactionCodeList[it.transactionCodeList.indexOf(
                                    CommonModel(details.code)
                                )].name
                            list.add(details)
                        }

                        transactionAdapter.differ.submitList(list)
                        viewModel.setTransactionProfile(list)

                        binding.txtViewNoEntry.isVisible = it.tpDetails.isEmpty()
                        binding.scrollView.isVisible = it.tpDetails.isNotEmpty()

                        binding.btnNext.isEnabled = it.tpDetails.isNotEmpty()

                        viewModel.insertTransactionProfile(it.tpDetails)
                    }
                }

                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        })

        transactionAdapter.setOnTextChangeListener { i, i2, i3 ->
            when (i3) {
                0 -> {
                    list[i].limitNoOfDailyTrn = i2
                }

                1 -> {
                    list[i].limitDailyTrnAmt = i2
                }

                2 -> {
                    list[i].limitNoOfMonthlyTrn = i2
                }

                3 -> {
                    list[i].limitMonthlyTrnAmt = i2
                }

                4 -> {
                    list[i].limitMaxTrnAmt = i2
                }
            }

            viewModel.setTransactionProfile(list)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}