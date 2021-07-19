package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.decode
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
import com.bumptech.glide.RequestManager
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

    @Inject
    lateinit var glide: RequestManager

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PhotoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnTakePhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        binding.btnBrowsePhoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        binding.btnTakeNidFront.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForNIDFrontResult.launch(intent)
                }

        }

        binding.btnBrowseNidFront.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForNIDFrontResult.launch(intent)
                }

        }

        binding.btnTakeNidBack.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForNIDBackResult.launch(intent)
                }

        }

        binding.btnBrowseNidBack.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForNIDBackResult.launch(intent)
                }

        }

        binding.btnTakeGuardianPhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForGuardianPhotoResult.launch(intent)
                }

        }

        binding.btnBrowseGuardianPhoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForGuardianPhotoResult.launch(intent)
                }

        }

        binding.btnTakeGuardianNidFront.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForGuardianFrontResult.launch(intent)
                }

        }

        binding.btnBrowseGuardianNidFront.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForGuardianFrontResult.launch(intent)
                }

        }

        binding.btnTakeGuardianNidBack.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForGuardianBackResult.launch(intent)
                }

        }

        binding.btnBrowseGuardianNidBack.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .createIntent { intent ->
                    startForGuardianBackResult.launch(intent)
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

        binding.btnBrowseSignature.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForSignatureResult.launch(intent)
                }
        }

        binding.btnTakeGuardianSignature.setOnClickListener {
            resultLauncherGuardianSignature.launch(
                Intent(
                    requireContext(),
                    SignatureActivity::class.java
                )
            )
        }

        binding.btnBrowseGuardianSignature.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForGuardianSignatureResult.launch(intent)
                }
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
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                documentList
                            )
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
            if (!it) {
                binding.txtViewGuardianNidBack.visibility = View.INVISIBLE
                binding.imgViewGuardianNidBack.visibility = View.INVISIBLE
                binding.btnTakeGuardianNidBack.visibility = View.INVISIBLE
                binding.btnBrowseGuardianNidBack.visibility = View.INVISIBLE
            } else {
                binding.txtViewGuardianNidBack.visibility = View.VISIBLE
                binding.imgViewGuardianNidBack.visibility = View.VISIBLE
                binding.btnTakeGuardianNidBack.visibility = View.VISIBLE
                binding.btnBrowseGuardianNidBack.visibility = View.VISIBLE
            }
        })

        var attachNumber = 0

        viewModel.getUserPhoto().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewPhoto)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getDocumentFront().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewNidFront)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getDocumentBack().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewNidBack)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getSignature().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewSignature)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getGuardianPhoto().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewGuardianPhoto)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getGuardianDocumentFront().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewGuardianNidFront)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getGuardianDocumentBack().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewGuardianNidBack)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getGuardianSignature().observe(viewLifecycleOwner, {
            glide.load(decode(it, DEFAULT)).into(binding.imgViewGuardianSignature)
            attachNumber++
            nextButtonEnable(attachNumber)
        })

        viewModel.getGuardianDocumentType().observe(viewLifecycleOwner, {
            if (documentList.isNotEmpty()) binding.spinnerGuardianDocumentType.setSelection(
                documentList.indexOf(
                    DocumentConfigData(it)
                )
            )
        })

        binding.spinnerGuardianDocumentType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setBackImage(
                        documentList[position].isBackRequired,
                        documentList[position].id
                    )
                }
            }
    }

    private var resultLauncherSignature =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val byteArray = result.data?.getByteArrayExtra(Constant.SIGNATURE_INFO)

                byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }?.let {

                    Base64Image.encode(it) { base64 ->
                        base64?.let { value ->
                            viewModel.setUserSignature(value)
                        }
                    }
                }
            }
        }

    private var resultLauncherGuardianSignature =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val byteArray = result.data?.getByteArrayExtra(Constant.SIGNATURE_INFO)

                byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }?.let {

                    Base64Image.encode(it) { base64 ->
                        base64?.let { value ->
                            viewModel.setGuardianSignature(value)
                        }
                    }
                }
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
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setUserPhoto(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
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
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setUserDocumentFront(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
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
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setUserDocumentBack(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForGuardianPhotoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setGuardianPhoto(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForGuardianFrontResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setGuardianDocumentFront(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForGuardianBackResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setGuardianDocumentBack(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
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
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setUserSignature(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForGuardianSignatureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                fileUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            fileUri
                        )
                    }

                    Base64Image.encode(bitmap) { base64 ->
                        base64?.let {
                            viewModel.setGuardianSignature(it)
                        }
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        requireActivity(),
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
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