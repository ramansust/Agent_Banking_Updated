package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

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
import com.datasoft.abs.data.dto.createCustomer.DocumentInfo
import com.datasoft.abs.databinding.FragmentDocumentsBinding
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DocumentsFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: DocumentsViewModel by activityViewModels()
    private var _binding: FragmentDocumentsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var documentAdapter: DocumentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(6)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(4)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        customerViewModel.requestVisibility(true)

        viewModel.getSavedData().observe(viewLifecycleOwner, {
            documentAdapter.differ.submitList(it)

            binding.txtViewNoEntry.isVisible = it.size <= 0
            binding.recyclerView.isVisible = it.size > 0

            binding.btnNext.isEnabled = it.size > 0

            binding.txtViewDocumentName.isVisible = it.size > 0
            binding.txtViewTracingId.isVisible = it.size > 0
            binding.txtViewIssueDate.isVisible = it.size > 0
            binding.txtViewExpiryDate.isVisible = it.size > 0
            binding.txtViewDescription.isVisible = it.size > 0
            binding.txtViewAction.isVisible = it.size > 0
        })

        customerViewModel.getAddListener().observe(viewLifecycleOwner, {
            if (it) {
                resultLauncher.launch(Intent(requireContext(), DocumentActivity::class.java))
            }
        })

        documentAdapter.setOnItemClickListener {
            viewModel.removeData(it)
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.notifyData(result.data?.getSerializableExtra(Constant.DOCUMENT_INFO) as DocumentInfo)
            }
        }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}