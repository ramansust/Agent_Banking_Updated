package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.signature

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.SignatureFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignatureFragment : Fragment() {

    companion object {
        fun newInstance() = SignatureFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
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

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    binding.imgView.setImageURI(fileUri)

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, fileUri))
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                    }

                    viewModel.setSignature(bitmap)
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