package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.datasoft.abs.R
import com.datasoft.abs.databinding.FragmentTransactionBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.BALANCE_FORMAT
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.TransactionAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAccountDetails().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let {
                        binding.txtViewAccountName.text = it.accountTitle
                        binding.txtViewAccountNumber.text =
                            resources.getString(R.string.account_no) + ": " + it.accountNo
                        binding.txtViewAccountType.text =
                            resources.getString(R.string.account_type) + ": " + it.acType

                        binding.txtViewBalance.text =
                            DecimalFormat(BALANCE_FORMAT).format(it.balance).toString()

                        viewModel.setAccountNumber(it.accountNo!!)

                        glide.load(
                            Base64.decode(
                                it.profileImage!!.substring(
                                    it.profileImage.indexOf(
                                        ","
                                    ) + 1
                                ), Base64.DEFAULT
                            )
                        ).apply(RequestOptions().circleCrop())
                            .into(binding.imgViewPhoto)
                    }
                }

                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
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

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }
}