package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

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
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.databinding.NomineeFragmentBinding
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NomineeFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val nomineeViewModel: NomineeViewModel by activityViewModels()
    private var _binding: NomineeFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isAddEnabled = false

    @Inject
    lateinit var nomineeAdapter: NomineeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NomineeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(true)

        setupRecyclerView()

        nomineeViewModel.getSavedData().observe(viewLifecycleOwner, {
            nomineeAdapter.differ.submitList(it)

            binding.txtViewNoEntry.isVisible = it.size <= 0
            binding.recyclerView.isVisible = it.size > 0

            binding.btnNext.isEnabled = it.size > 0
        })

        accountViewModel.getAddListener().observe(viewLifecycleOwner, {
            if(it && isAddEnabled) {
                resultLauncher.launch(Intent(requireContext(), NomineeActivity::class.java))
            }

            isAddEnabled = true
        })

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(3)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(1)
        }

        nomineeAdapter.setOnItemClickListener {
            nomineeViewModel.removeData(it)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            nomineeViewModel.notifyData(result.data?.getSerializableExtra(Constant.NOMINEE_INFO) as Nominee)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = nomineeAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}