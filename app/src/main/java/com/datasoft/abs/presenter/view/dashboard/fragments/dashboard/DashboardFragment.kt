package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.datasoft.abs.databinding.FragmentDashboardBinding
import com.datasoft.abs.presenter.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textHome
        viewModel.getDashboardData().observe(viewLifecycleOwner, { response ->

            when(response) {
                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let { dashBoardResponse ->
                        textView.text = dashBoardResponse.vmAgentInfos.current_Balance.toString()
                    }
                }
                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        viewModel.requestDashboardData(10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }
}