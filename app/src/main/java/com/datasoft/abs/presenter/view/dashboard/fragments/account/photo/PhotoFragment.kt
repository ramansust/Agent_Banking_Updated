package com.datasoft.abs.presenter.view.dashboard.fragments.account.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.PhotoFragmentBinding
import com.datasoft.abs.presenter.utils.Photos
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoFragment()
    }

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
            takePhoto.launch()
        }

        return root
    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it == null)
                return@registerForActivityResult

            val isSavedSuccessfully =
                photos.savePhotoToInternalStorage(UUID.randomUUID().toString(), it)
            if (isSavedSuccessfully) {
                binding.imgView.setImageBitmap(it)
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