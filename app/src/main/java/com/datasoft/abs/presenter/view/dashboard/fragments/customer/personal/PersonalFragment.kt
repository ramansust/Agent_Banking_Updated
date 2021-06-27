package com.datasoft.abs.presenter.view.dashboard.fragments.customer.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.PersonalFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFragment : Fragment() {

    companion object {
        fun newInstance() = PersonalFragment()
    }

    private val viewModel: PersonalViewModel by activityViewModels()
    private var _binding: PersonalFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = PersonalFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        viewModel.getDashboardData().observe(viewLifecycleOwner, Observer {
//            textView.text = it?.totalApplied.toString()
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}