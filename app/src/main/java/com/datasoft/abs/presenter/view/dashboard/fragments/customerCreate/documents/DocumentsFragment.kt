package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.SignatureFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentsFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentsFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: DocumentsViewModel by activityViewModels()
    private var _binding: SignatureFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SignatureFragmentBinding.inflate(inflater, container, false)

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

        viewModel.getSavedSignature().observe(viewLifecycleOwner, {
            binding.imgView.setImageBitmap(it)
        })

        binding.signaturePad.setOnSignedListener(object : OnSignedListener {

            override fun onStartSigning() {

            }

            override fun onSigned() {
                binding.btnTake.isEnabled = true
                binding.btnClear.isEnabled = true
            }

            override fun onClear() {
                binding.btnTake.isEnabled = false
                binding.btnClear.isEnabled = false
            }
        })

        binding.btnClear.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.btnTake.setOnClickListener {
            val signatureBitmap: Bitmap = binding.signaturePad.signatureBitmap
            binding.imgView.setImageBitmap(signatureBitmap)
            viewModel.setSignature(signatureBitmap)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}