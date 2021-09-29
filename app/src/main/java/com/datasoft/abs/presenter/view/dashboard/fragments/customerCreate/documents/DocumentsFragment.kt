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
import com.datasoft.abs.data.source.local.db.entity.customer.toRelatedDoc
import com.datasoft.abs.databinding.FragmentDocumentsBinding
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
    private var isAddEnabled = false

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

        viewModel.getDocuments().observe(viewLifecycleOwner, {
            documentAdapter.differ.submitList(it.map {
                it.toRelatedDoc()
            })

            binding.txtViewNoEntry.isVisible = it.isEmpty()
            binding.recyclerView.isVisible = it.isNotEmpty()

            binding.btnNext.isEnabled = it.isNotEmpty()
        })

        customerViewModel.getAddListener().observe(viewLifecycleOwner, {
            if (it && isAddEnabled) {
                resultLauncher.launch(Intent(requireContext(), DocumentActivity::class.java))
            }

            isAddEnabled = true
        })

        documentAdapter.setOnItemClickListener {
            viewModel.removeData(1)
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                viewModel.notifyData(1)
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

    /*private fun fileUriToString(relatedDoc: RelatedDoc) {

        var front = false
        var back = false

        val bitmapFront = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireActivity().contentResolver,
                    Uri.parse(relatedDoc.frontSideImage)
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                Uri.parse(relatedDoc.frontSideImage)
            )
        }

        if (relatedDoc.backSideImage.isNotEmpty()) {
            val bitmapBack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireActivity().contentResolver,
                        Uri.parse(relatedDoc.backSideImage)
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    Uri.parse(relatedDoc.backSideImage)
                )
            }

            Base64Image.encode(bitmapBack) { base64 ->
                base64?.let {
                    relatedDoc.backSideImage = it
                    back = true

                    if (front && back)
                        viewModel.notifyData(relatedDoc)
                }
            }
        } else {
            back = true
        }

        Base64Image.encode(bitmapFront) { base64 ->
            base64?.let {
                relatedDoc.frontSideImage = it
                front = true

                if (front && back)
                    viewModel.notifyData(relatedDoc)
            }
        }
    }*/
}