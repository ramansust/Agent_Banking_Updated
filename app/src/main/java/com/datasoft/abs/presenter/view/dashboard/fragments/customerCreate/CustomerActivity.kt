package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityCustomerBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc.KYCViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : BaseActivity() {

    private val customerViewModel: CustomerViewModel by viewModels()
    private val kycViewModel: KYCViewModel by viewModels()
    private lateinit var binding: ActivityCustomerBinding
    private var navController: NavController? = null

    override fun observeViewModel() {

        customerViewModel.getAddVisibility().observe(this, {
            if (it)
                showAddButton()
            else
                hideAddButton()
        })

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
                    navController?.navigate(R.id.card_view_fingerprint)
                    setResource(binding.appBarCustomer.customerContent.fingerprint.cardView, null)
                }
                5 -> {
                    navController?.navigate(R.id.card_view_documents)
                    setResource(binding.appBarCustomer.customerContent.documents.cardView, null)
                }
                6 -> {
                    navController?.navigate(R.id.card_view_kyc)
                    setResource(binding.appBarCustomer.customerContent.kyc.cardView, null)
                }
                7 -> {
                    navController?.navigate(R.id.card_view_review)
                    setResource(binding.appBarCustomer.customerContent.review.cardView, null)
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
                        binding.appBarCustomer.customerContent.fingerprint.cardView,
                        true
                    )
                    5 -> setResource(
                        binding.appBarCustomer.customerContent.documents.cardView,
                        true
                    )
                    6 -> setResource(
                        binding.appBarCustomer.customerContent.kyc.cardView,
                        true
                    )
                    7 -> setResource(
                        binding.appBarCustomer.customerContent.review.cardView,
                        true
                    )
                }
            }

            for (i in it + 1..7) {
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
                        binding.appBarCustomer.customerContent.fingerprint.cardView,
                        false
                    )
                    5 -> setResource(
                        binding.appBarCustomer.customerContent.documents.cardView,
                        false
                    )
                    6 -> setResource(binding.appBarCustomer.customerContent.kyc.cardView, false)
                    7 -> setResource(binding.appBarCustomer.customerContent.review.cardView, false)
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

        customerViewModel.configData()
        kycViewModel.configData()
        customerViewModel.setDocumentList()

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

        binding.appBarCustomer.customerContent.fingerprint.cardView.setOnClickListener {
            setCurrentState(4)
        }

        binding.appBarCustomer.customerContent.documents.cardView.setOnClickListener {
            setCurrentState(5)
        }

        binding.appBarCustomer.customerContent.kyc.cardView.setOnClickListener {
            setCurrentState(6)
        }
        binding.appBarCustomer.customerContent.review.cardView.setOnClickListener {
            setCurrentState(7)
        }

        binding.appBarCustomer.btnCross.setOnClickListener {
            showConfirmation()
        }

        binding.appBarCustomer.btnSave.setOnClickListener {
            customerViewModel.requestListener(true)
        }

        setTitle()
    }

    override fun onBackPressed() {
        showConfirmation()
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

        binding.appBarCustomer.customerContent.fingerprint.textViewTitle.text =
            getString(R.string.fingerprint)
        binding.appBarCustomer.customerContent.fingerprint.txtViewSl.text =
            getString(R.string.five)

        binding.appBarCustomer.customerContent.documents.textViewTitle.text =
            getString(R.string.documents)
        binding.appBarCustomer.customerContent.documents.txtViewSl.text =
            getString(R.string.six)

        binding.appBarCustomer.customerContent.kyc.textViewTitle.text =
            getString(R.string.kyc)
        binding.appBarCustomer.customerContent.kyc.txtViewSl.text =
            getString(R.string.seven)

        binding.appBarCustomer.customerContent.review.textViewTitle.text =
            getString(R.string.review)
        binding.appBarCustomer.customerContent.review.txtViewSl.text =
            getString(R.string.eight)
    }

    private fun setResource(cardView: CardView, isActive: Boolean?) {

        if (isActive == null) {
            val img: ImageView = cardView.findViewById(R.id.img_view_completed)
            img.visibility = View.INVISIBLE
            cardView.setBackgroundColor(getColor(R.color.white))
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

    private fun showAddButton() {
        binding.appBarCustomer.btnSave.visibility = View.VISIBLE
    }

    private fun hideAddButton() {
        binding.appBarCustomer.btnSave.visibility = View.INVISIBLE
    }

    private fun showConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(resources.getString(R.string.confirmation_message))
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}