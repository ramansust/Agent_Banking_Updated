package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.NomineeFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NomineeFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: NomineeViewModel by activityViewModels()
    private var _binding: NomineeFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var isAddEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NomineeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountViewModel.requestVisibility(true)

        accountViewModel.getAddListener().observe(viewLifecycleOwner, {
            if(it && isAddEnabled) {
//                resultLauncher.launch(Intent(requireContext(), AddressActivity::class.java))
            }

            isAddEnabled = true
        })

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(3)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}