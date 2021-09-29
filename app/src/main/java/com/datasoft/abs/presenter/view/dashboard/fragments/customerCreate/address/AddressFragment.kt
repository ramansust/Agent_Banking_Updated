package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.databinding.AddressFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val addressViewModel: AddressViewModel by activityViewModels()
    private var _binding: AddressFragmentBinding? = null

    @Inject
    lateinit var addressAdapter: AddressListAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isAddEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AddressFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(true)

        setupRecyclerView()

        addressViewModel.getAddresses().observe(viewLifecycleOwner, {
            addressAdapter.differ.submitList(it)

            binding.txtViewNoEntry.isVisible = it.isEmpty()
            binding.recyclerView.isVisible = it.isNotEmpty()

            binding.btnNext.isEnabled = it.isNotEmpty()
        })

        customerViewModel.getAddListener().observe(viewLifecycleOwner, {
            if(it && isAddEnabled) {
                resultLauncher.launch(Intent(requireContext(), AddressActivity::class.java))
            }

            isAddEnabled = true
        })

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(1)
        }

        addressAdapter.setOnItemClickListener {
            addressViewModel.removeAddress(it.id)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
//            addressViewModel.notifyData(result.data?.getSerializableExtra(ADDRESS_INFO) as AddressInfo)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = addressAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}