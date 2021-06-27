package com.datasoft.abs.presenter.view.dashboard.fragments.customer

import android.os.Bundle
import androidx.navigation.findNavController
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityCustomerBinding
import com.datasoft.abs.presenter.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : BaseActivity() {

    private lateinit var binding: ActivityCustomerBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController(R.id.nav_host_fragment_content_customer)

        binding.appBarCustomer.customerContent.cardViewGeneralInfo.setOnClickListener {
            navController.navigate(R.id.card_view_general_info)
        }

        binding.appBarCustomer.customerContent.cardViewPersonalInfo.setOnClickListener {
            navController.navigate(R.id.card_view_personal_info)
        }

        binding.appBarCustomer.customerContent.cardViewAddress.setOnClickListener {
            navController.navigate(R.id.card_view_address)
        }

        binding.appBarCustomer.customerContent.cardViewPhotoNid.setOnClickListener {
            navController.navigate(R.id.card_view_photo_nid)
        }

        binding.appBarCustomer.customerContent.cardViewSignature.setOnClickListener {
            navController.navigate(R.id.card_view_signature)
        }

        binding.appBarCustomer.customerContent.cardViewFingerprint.setOnClickListener {
            navController.navigate(R.id.card_view_fingerprint)
        }

        binding.appBarCustomer.customerContent.cardViewNominee.setOnClickListener {
            navController.navigate(R.id.card_view_nominee)
        }

        binding.appBarCustomer.btnCross.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}