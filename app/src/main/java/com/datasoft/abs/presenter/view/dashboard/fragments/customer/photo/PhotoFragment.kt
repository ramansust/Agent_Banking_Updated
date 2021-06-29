package com.datasoft.abs.presenter.view.dashboard.fragments.customer.photo

import android.app.Activity
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.PhotoFragmentBinding
import com.datasoft.abs.presenter.utils.Photos
import com.datasoft.abs.presenter.view.dashboard.fragments.customer.CustomerViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoFragment()
    }

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: PhotoViewModel by activityViewModels()
    private var _binding: PhotoFragmentBinding? = null

    @Inject
    lateinit var photos: Photos

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PhotoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTakePhoto.setOnClickListener {
//            takePhotoUsingCamera.launch()
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        binding.btnTakeNidFront.setOnClickListener {
//            takePhotoUsingCamera.launch()
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForNIDFrontResult.launch(intent)
                }

        }

        binding.btnTakeNidBack.setOnClickListener {
//            takePhotoUsingCamera.launch()
            ImagePicker.with(this)
                .crop()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForNIDBackResult.launch(intent)
                }

        }

        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(4)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(2)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSavedPhoto().observe(viewLifecycleOwner, {
            binding.imgViewPhoto.setImageBitmap(it)
        })

        viewModel.getSavedNIDFront().observe(viewLifecycleOwner, {
            binding.imgViewNidFront.setImageBitmap(it)
        })

        viewModel.getSavedNIDBack().observe(viewLifecycleOwner, {
            binding.imgViewNidBack.setImageBitmap(it)
        })
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    binding.imgViewPhoto.setImageURI(fileUri)

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, fileUri))
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                    }

                    viewModel.setPhoto(bitmap)

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            Log.e("base64", "_______$it")
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForNIDFrontResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    binding.imgViewNidFront.setImageURI(fileUri)

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, fileUri))
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                    }

                    viewModel.setNIDFront(bitmap)

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            Log.e("base64", "_______$it")
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForNIDBackResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    binding.imgViewNidBack.setImageURI(fileUri)

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, fileUri))
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, fileUri)
                    }

                    viewModel.setNIDBack(bitmap)

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            Log.e("base64", "_______$it")
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val takePhotoUsingCamera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it == null)
                return@registerForActivityResult

            val isSavedSuccessfully =
                photos.savePhotoToInternalStorage(UUID.randomUUID().toString(), it)
            if (isSavedSuccessfully) {
                binding.imgViewPhoto.setImageBitmap(it)
                Toast.makeText(requireContext(), "Photo saved successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed to save photo", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}