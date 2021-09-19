package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.introducer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.IntroducerFragmentBinding
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroducerFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: IntroducerViewModel by activityViewModels()
    private var _binding: IntroducerFragmentBinding? = null

    @Inject
    lateinit var toastHelper: ToastHelper

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroducerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        viewModel.getIntroducerData().observe(viewLifecycleOwner, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {

                    Status.SUCCESS -> {
                        result.data?.let {
                            binding.btnNext.isEnabled = true
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

            response?.peekContent()?.let { result ->

                when (result.status) {

                    Status.SUCCESS -> {
                        result.data?.let {
                            binding.txtViewAccountTitle.text = it.accountTitle
                            binding.txtViewName.text = it.fullName
                            binding.txtViewAccountNumber.text = it.accountNumber

                            binding.btnNext.isEnabled = true
                        }
                    }

                    Status.ERROR -> {

                    }

                    Status.LOADING -> {

                    }

                }
            }
        })

        val relationList = mutableListOf<CommonModel>()

        accountViewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {

                        relationList.addAll(it.relationList)
                        binding.spinnerRelation.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                relationList
                            )
                    }
                }
                Status.ERROR -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                Status.LOADING -> {
                }
            }
        })

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(4)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(2)
        }

        binding.spinnerRelation.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setRelationId(relationList[position].id)
                }
            }

//        binding.edTxtIntroducer.addTextChangedListener(textWatcher) // 1001120013386

        binding.imgViewSearch.setOnClickListener {
            viewModel.introducerData(binding.edTxtIntroducer.text.trim().toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}