package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.datasoft.abs.databinding.FragmentEftnTransactionDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EFTNTransactionDetailsFragment : Fragment() {

    private var _binding: FragmentEftnTransactionDetailsBinding? = null
    private val args: EFTNTransactionDetailsFragmentArgs by navArgs()

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

        /*binding.edTxtAccountNumber.setText()
        binding.edTxtAccountTitle.setText()
        binding.edTxtAccountType.setText()
        binding.edTxtAmount.setText()
        binding.edTxtReceiverAccountNo.setText()
        binding.edTxtReceiverName.setText()
        binding.edTxtReceiverBank.setText()
        binding.edTxtBranchRouting.setText()

        glide.load(
            Base64.decode(
                it.profileImage!!.substring(
                    it.profileImage.indexOf(
                        ","
                    ) + 1
                ), Base64.DEFAULT
            )
        ).apply(RequestOptions().circleCrop())
            .into(binding.imgViewSenderPhoto)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}