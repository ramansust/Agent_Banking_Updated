package com.datasoft.abs.presenter.view.dashboard.fragments.customer

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityCustomerBinding
import com.datasoft.abs.presenter.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : BaseActivity() {

    private val customerViewModel: CustomerViewModel by viewModels()
    private lateinit var binding: ActivityCustomerBinding
    private var navController: NavController? = null

    override fun observeViewModel() {
        customerViewModel.getCurrentStep().observe(this, {
            when (it) {
                0 -> {
                    navController?.navigate(R.id.card_view_general_info)
                    setResource(binding.appBarCustomer.customerContent.generalInfo.cardView, null)
                }
                1 -> {
                    navController?.navigate(R.id.card_view_personal_info)
                    setResource(binding.appBarCustomer.customerContent.personalInfo.cardView, null)
                }
                2 -> {
                    navController?.navigate(R.id.card_view_address)
                    setResource(binding.appBarCustomer.customerContent.address.cardView, null)
                }
                3 -> {
                    navController?.navigate(R.id.card_view_photo_nid)
                    setResource(binding.appBarCustomer.customerContent.photoNid.cardView, null)
                }
                4 -> {
                    navController?.navigate(R.id.card_view_signature)
                    setResource(binding.appBarCustomer.customerContent.signature.cardView, null)
                }
                5 -> {
                    navController?.navigate(R.id.card_view_fingerprint)
                    setResource(binding.appBarCustomer.customerContent.fingerprint.cardView, null)
                }
                6 -> {
                    navController?.navigate(R.id.card_view_nominee)
                    setResource(binding.appBarCustomer.customerContent.nominee.cardView, null)
                }
            }

            for (i in 0 until it) {
                when (i) {
                    0 -> setResource(
                        binding.appBarCustomer.customerContent.generalInfo.cardView,
                        true
                    )
                    1 -> setResource(
                        binding.appBarCustomer.customerContent.personalInfo.cardView,
                        true
                    )
                    2 -> setResource(
                        binding.appBarCustomer.customerContent.address.cardView,
                        true
                    )
                    3 -> setResource(
                        binding.appBarCustomer.customerContent.photoNid.cardView,
                        true
                    )
                    4 -> setResource(
                        binding.appBarCustomer.customerContent.signature.cardView,
                        true
                    )
                    5 -> setResource(
                        binding.appBarCustomer.customerContent.fingerprint.cardView,
                        true
                    )
                    6 -> setResource(
                        binding.appBarCustomer.customerContent.nominee.cardView,
                        true
                    )
                }
            }

            for (i in it + 1..6) {
                when (i) {
                    0 -> setResource(
                        binding.appBarCustomer.customerContent.generalInfo.cardView,
                        false
                    )
                    1 -> setResource(
                        binding.appBarCustomer.customerContent.personalInfo.cardView,
                        false
                    )
                    2 -> setResource(binding.appBarCustomer.customerContent.address.cardView, false)
                    3 -> setResource(
                        binding.appBarCustomer.customerContent.photoNid.cardView,
                        false
                    )
                    4 -> setResource(
                        binding.appBarCustomer.customerContent.signature.cardView,
                        false
                    )
                    5 -> setResource(
                        binding.appBarCustomer.customerContent.fingerprint.cardView,
                        false
                    )
                    6 -> setResource(binding.appBarCustomer.customerContent.nominee.cardView, false)
                }
            }
        })
    }

    override fun initViewBinding() {
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController(R.id.nav_host_fragment_content_customer)
        setCurrentState(0)

        binding.appBarCustomer.customerContent.generalInfo.cardView.setOnClickListener {
            setCurrentState(0)
        }

        binding.appBarCustomer.customerContent.personalInfo.cardView.setOnClickListener {
            setCurrentState(1)
        }

        binding.appBarCustomer.customerContent.address.cardView.setOnClickListener {
            setCurrentState(2)
        }

        binding.appBarCustomer.customerContent.photoNid.cardView.setOnClickListener {
            setCurrentState(3)
        }

        binding.appBarCustomer.customerContent.signature.cardView.setOnClickListener {
            setCurrentState(4)
        }

        binding.appBarCustomer.customerContent.fingerprint.cardView.setOnClickListener {
            setCurrentState(5)
        }

        binding.appBarCustomer.customerContent.nominee.cardView.setOnClickListener {
            setCurrentState(6)
        }

        binding.appBarCustomer.btnCross.setOnClickListener {
            finish()
        }

        setTitle()
    }

    private fun setCurrentState(index: Int) {
        customerViewModel.requestCurrentStep(index)
    }

    private fun setTitle() {
        binding.appBarCustomer.customerContent.generalInfo.textViewTitle.text =
            getString(R.string.general_information)
        binding.appBarCustomer.customerContent.generalInfo.txtViewSl.text =
            getString(R.string.one)

        binding.appBarCustomer.customerContent.personalInfo.textViewTitle.text =
            getString(R.string.personal_information)
        binding.appBarCustomer.customerContent.personalInfo.txtViewSl.text =
            getString(R.string.two)

        binding.appBarCustomer.customerContent.address.textViewTitle.text =
            getString(R.string.address)
        binding.appBarCustomer.customerContent.address.txtViewSl.text =
            getString(R.string.three)

        binding.appBarCustomer.customerContent.photoNid.textViewTitle.text =
            getString(R.string.photo_nid)
        binding.appBarCustomer.customerContent.photoNid.txtViewSl.text =
            getString(R.string.four)

        binding.appBarCustomer.customerContent.signature.textViewTitle.text =
            getString(R.string.signature)
        binding.appBarCustomer.customerContent.signature.txtViewSl.text =
            getString(R.string.five)

        binding.appBarCustomer.customerContent.fingerprint.textViewTitle.text =
            getString(R.string.fingerprint)
        binding.appBarCustomer.customerContent.fingerprint.txtViewSl.text =
            getString(R.string.six)

        binding.appBarCustomer.customerContent.nominee.textViewTitle.text =
            getString(R.string.nominee)
        binding.appBarCustomer.customerContent.nominee.txtViewSl.text =
            getString(R.string.seven)
    }

    private fun setResource(cardView: CardView, isActive: Boolean?) {

        if (isActive == null) {
            val img: ImageView = cardView.findViewById(R.id.img_view_completed)
            img.visibility = View.INVISIBLE
            cardView.setBackgroundColor(getColor(R.color.purple_500))
        } else if (isActive) {
            val img: ImageView = cardView.findViewById(R.id.img_view_completed)
            img.visibility = View.VISIBLE
            cardView.setBackgroundColor(getColor(R.color.completed_step))
        } else if (!isActive) {
            val img: ImageView = cardView.findViewById(R.id.img_view_completed)
            img.visibility = View.INVISIBLE
            cardView.setBackgroundColor(getColor(R.color.purple_200))
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}