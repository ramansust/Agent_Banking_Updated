package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.DocumentConfigData
import com.datasoft.abs.databinding.PhotoFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Photos
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.personal.PersonalViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: PhotoViewModel by activityViewModels()
    private val personalViewModel: PersonalViewModel by activityViewModels()
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

        binding.btnTakeSignature.setOnClickListener {
            resultLauncherSignature.launch(Intent(requireContext(), SignatureActivity::class.java))
        }

        binding.btnTakeGuardianSignature.setOnClickListener {
            resultLauncherGuardianSignature.launch(Intent(requireContext(), SignatureActivity::class.java))
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        val documentList = mutableListOf<DocumentConfigData>()

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        documentList.addAll(it.documentConfigData)
                        binding.spinnerGuardianDocumentType.adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, documentList)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        personalViewModel.getCustomerAgeData().observe(viewLifecycleOwner, {

            (it < Constant.ADULT_AGE).apply {
                binding.txtViewMinorAttain.isVisible = this
                binding.txtViewGuardianInfo.isVisible = this
                binding.cardViewGuardianPhoto.isVisible = this
                binding.cardViewGuardianSignature.isVisible = this
                binding.cardViewGuardianDocuments.isVisible = this
                binding.spinnerGuardianDocumentType.isVisible = this
            }
        })

        viewModel.getBackImage().observe(viewLifecycleOwner, {
            if(!it) {
                binding.txtViewGuardianNidBack.visibility = View.INVISIBLE
                binding.imgViewGuardianNidBack.visibility = View.INVISIBLE
                binding.btnTakeGuardianNidBack.visibility = View.INVISIBLE
            } else {
                binding.txtViewGuardianNidBack.visibility = View.VISIBLE
                binding.imgViewGuardianNidBack.visibility = View.VISIBLE
                binding.btnTakeGuardianNidBack.visibility = View.VISIBLE
            }
        })

        var attachNumber = 0

        viewModel.getSavedPhoto().observe(viewLifecycleOwner, {
            binding.imgViewPhoto.setImageBitmap(it)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getSavedNIDFront().observe(viewLifecycleOwner, {
            binding.imgViewNidFront.setImageBitmap(it)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getSavedNIDBack().observe(viewLifecycleOwner, {
            binding.imgViewNidBack.setImageBitmap(it)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getSavedSignature().observe(viewLifecycleOwner, {
//            binding.imgView.setImageBitmap(it)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        binding.spinnerGuardianDocumentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setBackImage(documentList[position].isBackRequired)
            }
        }
    }

    private var resultLauncherSignature =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                viewModel.notifyData(result.data?.getSerializableExtra(Constant.DOCUMENT_INFO) as DocumentInfo)
            }
        }

    private var resultLauncherGuardianSignature =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                viewModel.notifyData(result.data?.getSerializableExtra(Constant.DOCUMENT_INFO) as DocumentInfo)
            }
        }

    private fun nextButtonEnable(attachNumber: Int) {
        binding.btnNext.isEnabled = attachNumber >= 4
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

    private val startForSignatureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
//                    binding.imgView.setImageURI(fileUri)

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