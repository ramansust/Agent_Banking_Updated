package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.databinding.OthersFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OthersFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: OthersViewModel by activityViewModels()
    private var _binding: OthersFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isClicked = false

    @Inject
    lateinit var otherFacilityAdapter: OtherFacilityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = OthersFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnNext.setOnClickListener {
            isClicked = true
            viewModel.setNotify(true)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(0)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        setupRecyclerView()

        viewModel.getOtherFacilities().observe(viewLifecycleOwner, {
            otherFacilityAdapter.differ.submitList(it)

            binding.btnNext.isEnabled = it.isNotEmpty()
        })

        viewModel.getNotifyData().observe(viewLifecycleOwner, {
            if (isClicked)
                accountViewModel.requestCurrentStep(2)
        })

        otherFacilityAdapter.setOnItemClickListener { id, isChecked ->
            viewModel.updateOtherFacility(id, isChecked)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = otherFacilityAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}