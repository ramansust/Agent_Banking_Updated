package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        viewModel.detailsData(args.transactionId.toString(), true)

        viewModel.getTransactionDetails().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        binding.edTxtAccountNumber.setText(it.senderAccNumber)
                        binding.edTxtAccountTitle.setText(it.accountTitle)
                        binding.edTxtAccountType.setText(it.accountType)
                        binding.edTxtAmount.setText(it.amount!!.toString())
                        binding.edTxtReceiverAccountNo.setText(it.receiverAccNumber)
                        binding.edTxtReceiverName.setText(it.receiverName)
                        binding.edTxtReceiverBank.setText(it.receiverBank)
                        binding.edTxtBranchRouting.setText(it.receiverRouting)

                        glide.load(
                            Base64.decode(
                                it.senderPhoto!!.substring(
                                    it.senderPhoto.indexOf(
                                        ","
                                    ) + 1
                                ), Base64.DEFAULT
                            )
                        ).apply(RequestOptions().circleCrop())
                            .into(binding.imgViewSenderPhoto)
                    }
                }

                is Resource.Loading -> {

                }

                is Resource.Error -> {

                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}