package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.datasoft.abs.databinding.OthersFragmentBinding
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: OthersViewModel by activityViewModels()
    private var _binding: OthersFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = OthersFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(2)
        }

        binding.btnBack.setOnClickListener {
            accountViewModel.requestCurrentStep(0)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChequeBook().observe(viewLifecycleOwner, {
            binding.switchChequeBook.isChecked = it
        })

        viewModel.getSMSBanking().observe(viewLifecycleOwner, {
            binding.switchSmsBanking.isChecked = it
        })

        viewModel.getDebitCard().observe(viewLifecycleOwner, {
            binding.switchDebitCard.isChecked = it
        })

        viewModel.getEStatement().observe(viewLifecycleOwner, {
            binding.switchEStatement.isChecked = it
        })

        viewModel.getInternetBanking().observe(viewLifecycleOwner, {
            binding.switchInternetBanking.isChecked = it
        })

        binding.switchChequeBook.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setChequeBook(isChecked)
        }

        binding.switchSmsBanking.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSMSBanking(isChecked)
        }

        binding.switchDebitCard.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDebitCard(isChecked)
        }

        binding.switchEStatement.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEStatement(isChecked)
        }

        binding.switchInternetBanking.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setInternetBanking(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}