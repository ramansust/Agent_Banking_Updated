package com.datasoft.abs.presenter.view.dashboard.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FragmentChangePasswordBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val viewModel: ProfileViewModel by activityViewModels()

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        viewModel.getChangePassword().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->
                when (result.status) {

                    Status.SUCCESS -> {
                        result.data?.let {
                            it.message.let { it1 -> toastHelper.sendToast(it1) }
                        }
                    }

                    Status.ERROR -> {
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                        }
                    }

                    Status.LOADING -> {

                    }
                }
            }
        })

        binding.btnUpdatePassword.setOnClickListener {
            viewModel.changePassword(
                binding.edTxtCurrentPassword.text.trim().toString(),
                binding.edTxtNewPassword.text.trim().toString(),
                binding.edTxtConfirmNewPassword.text.trim().toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}