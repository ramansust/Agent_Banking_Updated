package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentDocumentsBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: DocumentsViewModel by activityViewModels()
    private var _binding: FragmentDocumentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}