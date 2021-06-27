package com.datasoft.abs.presenter.view.dashboard.fragments.customer.signature

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.SignatureFragmentBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignatureFragment : Fragment() {

    companion object {
        fun newInstance() = SignatureFragment()
    }

    private val viewModel: SignatureViewModel by activityViewModels()
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

        binding.btnBrowse.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)         // Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  // Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    binding.imgView.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}