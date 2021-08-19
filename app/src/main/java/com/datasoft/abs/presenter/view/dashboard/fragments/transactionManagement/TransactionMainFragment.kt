package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentMainTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionMainFragment : Fragment() {

    private var _binding: FragmentMainTransactionBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewTransaction.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_transaction)
        }

        binding.cardViewDisbursement.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_disbursement)
        }

        binding.cardViewDeposit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_deposit)
        }

        binding.cardViewWithdraw.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_withdraw)
        }

        binding.cardViewBalance.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_balance)
        }

        binding.cardViewEftn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_eftn)
        }

        binding.cardViewRtgs.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_transaction_to_rtgs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}