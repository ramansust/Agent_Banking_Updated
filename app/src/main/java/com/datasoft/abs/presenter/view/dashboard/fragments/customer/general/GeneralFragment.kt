package com.datasoft.abs.presenter.view.dashboard.fragments.customer.general

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.datasoft.abs.databinding.GeneralFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private val viewModel: GeneralViewModel by viewModels()
    private var _binding: GeneralFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GeneralFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.txtView
        viewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when(response) {
                is Resource.Success -> {
//                    goneProgressBar()
                    response.data?.let { configResponse ->
                        textView.text = configResponse.message
                    }
                }
                is Resource.Error -> {
//                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
//                    showProgressBar()
                }
            }
        })

        viewModel.configData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}