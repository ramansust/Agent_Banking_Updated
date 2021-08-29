package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.datasoft.abs.databinding.FragmentEftnTransactionDetailsBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs.RTGSViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EFTNTransactionDetailsFragment : Fragment() {

    private var _binding: FragmentEftnTransactionDetailsBinding? = null
    private val args: EFTNTransactionDetailsFragmentArgs by navArgs()

    private val viewModel: RTGSViewModel by activityViewModels()
    private val eftnViewModel: EFTNViewModel by activityViewModels()

    @Inject
    lateinit var glide: RequestManager

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEftnTransactionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDetails(0)
        eftnViewModel.setDetails(0)

        viewModel.detailsData(args.transactionId.toString(), args.isRTGS)

        viewModel.getTransactionDetails().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        goneProgressBar()

                        binding.edTxtAccountNumber.setText(it.sendeAccNo)
                        binding.edTxtAccountTitle.setText(it.senderAccTitle)
                        binding.edTxtAccountType.setText(it.productName)
                        binding.edTxtAmount.setText(it.amount!!.toString())
                        binding.edTxtReceiverAccountNo.setText(it.receiverAccNo)
                        binding.edTxtReceiverName.setText(it.receiverName)
                        binding.edTxtReceiverBank.setText(it.bankName)
                        binding.edTxtBranchRouting.setText(it.branchName)

                        glide.load(
                            Base64.decode(
                                it.profilePicture!!.substring(
                                    it.profilePicture.indexOf(
                                        ","
                                    ) + 1
                                ), Base64.DEFAULT
                            )
                        ).apply(RequestOptions().circleCrop())
                            .into(binding.imgViewSenderPhoto)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
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