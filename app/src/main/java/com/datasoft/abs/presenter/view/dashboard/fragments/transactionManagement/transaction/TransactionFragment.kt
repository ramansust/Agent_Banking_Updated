package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentTransactionBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.TransactionAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val viewModel: TransactionViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: TransactionAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.deposit)
                1 -> tab.text = resources.getString(R.string.withdraw)
                2 -> tab.text = resources.getString(R.string.transfer)
                3 -> tab.text = resources.getString(R.string.balance_inquiry)
            }
        }.attach()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAccountDetails().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let {
                        binding.txtViewAccountName.text = it.accountTitle
                        binding.txtViewAccountNumber.text = it.accountNo
                        binding.txtViewAccountType.text = it.acType
                        binding.txtViewBalance.text = it.balance.toString()

                        glide.load(Base64.decode(it.profileImage, Base64.DEFAULT))
                            .into(binding.imgViewPhoto)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {

                }

            }
        })

        binding.imgViewSearch.setOnClickListener {
            viewModel.accountDetails(binding.edTxtAccountNo.text.trim().toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}