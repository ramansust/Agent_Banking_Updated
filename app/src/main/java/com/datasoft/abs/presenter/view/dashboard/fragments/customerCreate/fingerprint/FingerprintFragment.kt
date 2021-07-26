package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.fingerprint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.FingerprintFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FingerprintFragment : Fragment() {

    private val customerViewModel: CustomerViewModel by activityViewModels()
    private val viewModel: FingerprintViewModel by activityViewModels()
    private var _binding: FingerprintFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var fingerSize = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FingerprintFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerViewModel.requestVisibility(false)
        customerViewModel.requestListener(false)

        customerViewModel.getConfigData().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        fingerSize = it.fingerKey.size

                        when (fingerSize / 2) {
                            1 -> {
                                binding.cardViewLeft1.visibility = View.VISIBLE
                                binding.cardViewRight1.visibility = View.VISIBLE
                            }

                            2 -> {
                                binding.cardViewLeft1.visibility = View.VISIBLE
                                binding.cardViewRight1.visibility = View.VISIBLE

                                binding.cardViewLeft2.visibility = View.VISIBLE
                                binding.cardViewRight2.visibility = View.VISIBLE
                            }

                            3 -> {
                                binding.cardViewLeft1.visibility = View.VISIBLE
                                binding.cardViewRight1.visibility = View.VISIBLE

                                binding.cardViewLeft2.visibility = View.VISIBLE
                                binding.cardViewRight2.visibility = View.VISIBLE

                                binding.cardViewLeft3.visibility = View.VISIBLE
                                binding.cardViewRight3.visibility = View.VISIBLE
                            }

                            4 -> {
                                binding.cardViewLeft1.visibility = View.VISIBLE
                                binding.cardViewRight1.visibility = View.VISIBLE

                                binding.cardViewLeft2.visibility = View.VISIBLE
                                binding.cardViewRight2.visibility = View.VISIBLE

                                binding.cardViewLeft3.visibility = View.VISIBLE
                                binding.cardViewRight3.visibility = View.VISIBLE

                                binding.cardViewLeft4.visibility = View.VISIBLE
                                binding.cardViewRight4.visibility = View.VISIBLE
                            }

                            5 -> {
                                binding.cardViewLeft1.visibility = View.VISIBLE
                                binding.cardViewRight1.visibility = View.VISIBLE

                                binding.cardViewLeft2.visibility = View.VISIBLE
                                binding.cardViewRight2.visibility = View.VISIBLE

                                binding.cardViewLeft3.visibility = View.VISIBLE
                                binding.cardViewRight3.visibility = View.VISIBLE

                                binding.cardViewLeft4.visibility = View.VISIBLE
                                binding.cardViewRight4.visibility = View.VISIBLE

                                binding.cardViewLeft5.visibility = View.VISIBLE
                                binding.cardViewRight5.visibility = View.VISIBLE
                            }
                        }
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

        viewModel.getFingerList().observe(viewLifecycleOwner, {
            when {
                it.size == fingerSize -> {
                    binding.btnNext.isEnabled = true
                }
                it.size < fingerSize / 2 -> {
                    when(it.size) {
                        1 -> binding.btnScanLeft2.isEnabled = true
                        2 -> binding.btnScanLeft3.isEnabled = true
                        3 -> binding.btnScanLeft4.isEnabled = true
                        4 -> binding.btnScanLeft5.isEnabled = true
                    }
                }
                it.size >= fingerSize / 2 -> {
                    when(it.size + 1 - fingerSize / 2) {
                        1 -> binding.btnScanRight1.isEnabled = true
                        2 -> binding.btnScanRight2.isEnabled = true
                        3 -> binding.btnScanRight3.isEnabled = true
                        4 -> binding.btnScanRight4.isEnabled = true
                        5 -> binding.btnScanRight5.isEnabled = true
                    }
                }
            }
        })

        binding.btnScanLeft1.setOnClickListener {
            viewModel.setFinger(0, "0")
        }

        binding.btnScanLeft2.setOnClickListener {
            viewModel.setFinger(1, "1")
        }

        binding.btnScanLeft3.setOnClickListener {
            viewModel.setFinger(2, "2")
        }

        binding.btnScanLeft4.setOnClickListener {
            viewModel.setFinger(3, "3")
        }

        binding.btnScanLeft5.setOnClickListener {
            viewModel.setFinger(4, "4")
        }

        binding.btnScanRight1.setOnClickListener {
            viewModel.setFinger(5, "5")
        }

        binding.btnScanRight2.setOnClickListener {
            viewModel.setFinger(6, "6")
        }

        binding.btnScanRight3.setOnClickListener {
            viewModel.setFinger(7, "7")
        }

        binding.btnScanRight4.setOnClickListener {
            viewModel.setFinger(8, "8")
        }

        binding.btnScanRight5.setOnClickListener {
            viewModel.setFinger(9, "9")
        }


        binding.btnNext.setOnClickListener {
            customerViewModel.requestCurrentStep(5)
        }

        binding.btnBack.setOnClickListener {
            customerViewModel.requestCurrentStep(3)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}