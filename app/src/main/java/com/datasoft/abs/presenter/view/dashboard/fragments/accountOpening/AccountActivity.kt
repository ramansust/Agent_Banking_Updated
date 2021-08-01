package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityAccountBinding
import com.datasoft.abs.presenter.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseActivity() {

    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var binding: ActivityAccountBinding
    private var navController: NavController? = null

    override fun observeViewModel() {
        accountViewModel.getCurrentStep().observe(this, {
            when (it) {
                0 -> {
                    navController?.navigate(R.id.card_view_account_info)
                    setResource(binding.appBarAccount.accountContent.accountInfo.cardView, null)
                }
                1 -> {
                    navController?.navigate(R.id.card_view_others_info)
                    setResource(binding.appBarAccount.accountContent.othersInfo.cardView, null)
                }
                2 -> {
                    navController?.navigate(R.id.card_view_nominee)
                    setResource(binding.appBarAccount.accountContent.nominee.cardView, null)
                }
                3 -> {
                    navController?.navigate(R.id.card_view_introducer)
                    setResource(binding.appBarAccount.accountContent.introducer.cardView, null)
                }
                4 -> {
                    navController?.navigate(R.id.card_view_transaction_profile)
                    setResource(binding.appBarAccount.accountContent.transactionProfile.cardView, null)
                }
                5 -> {
                    navController?.navigate(R.id.card_view_review)
                    setResource(binding.appBarAccount.accountContent.review.cardView, null)
                }
            }

            for (i in 0 until it) {
                when (i) {
                    0 -> setResource(
                        binding.appBarAccount.accountContent.accountInfo.cardView,
                        true
                    )
                    1 -> setResource(
                        binding.appBarAccount.accountContent.othersInfo.cardView,
                        true
                    )
                    2 -> setResource(
                        binding.appBarAccount.accountContent.nominee.cardView,
                        true
                    )
                    3 -> setResource(
                        binding.appBarAccount.accountContent.introducer.cardView,
                        true
                    )
                    4 -> setResource(
                        binding.appBarAccount.accountContent.transactionProfile.cardView,
                        true
                    )
                    5 -> setResource(
                        binding.appBarAccount.accountContent.review.cardView,
                        true
                    )
                }
            }

            for (i in it + 1..6) {
                when (i) {
                    0 -> setResource(
                        binding.appBarAccount.accountContent.accountInfo.cardView,
                        false
                    )
                    1 -> setResource(
                        binding.appBarAccount.accountContent.othersInfo.cardView,
                        false
                    )
                    2 -> setResource(binding.appBarAccount.accountContent.nominee.cardView, false)
                    3 -> setResource(
                        binding.appBarAccount.accountContent.introducer.cardView,
                        false
                    )
                    4 -> setResource(
                        binding.appBarAccount.accountContent.transactionProfile.cardView,
                        false
                    )
                    5 -> setResource(
                        binding.appBarAccount.accountContent.review.cardView,
                        false
                    )
                }
            }
        })
    }

    override fun initViewBinding() {
        binding = ActivityAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountViewModel.accountConfigData()

        navController = findNavController(R.id.nav_host_fragment_content_account)
        setCurrentState(0)

        binding.appBarAccount.accountContent.accountInfo.cardView.setOnClickListener {
            setCurrentState(0)
        }

        binding.appBarAccount.accountContent.othersInfo.cardView.setOnClickListener {
            setCurrentState(1)
        }

        binding.appBarAccount.accountContent.nominee.cardView.setOnClickListener {
            setCurrentState(2)
        }

        binding.appBarAccount.accountContent.introducer.cardView.setOnClickListener {
            setCurrentState(3)
        }

        binding.appBarAccount.accountContent.transactionProfile.cardView.setOnClickListener {
            setCurrentState(4)
        }

        binding.appBarAccount.accountContent.review.cardView.setOnClickListener {
            setCurrentState(5)
        }

        binding.appBarAccount.btnCross.setOnClickListener {
            finish()
        }

        setTitle()
    }

    private fun setCurrentState(index: Int) {
        accountViewModel.requestCurrentStep(index)
    }

    private fun setTitle() {
        binding.appBarAccount.accountContent.accountInfo.textViewTitle.text =
            getString(R.string.account_information)
        binding.appBarAccount.accountContent.accountInfo.txtViewSl.text =
            getString(R.string.one)

        binding.appBarAccount.accountContent.othersInfo.textViewTitle.text =
            getString(R.string.others_customer)
        binding.appBarAccount.accountContent.othersInfo.txtViewSl.text =
            getString(R.string.two)

        binding.appBarAccount.accountContent.nominee.textViewTitle.text =
            getString(R.string.nominee)
        binding.appBarAccount.accountContent.nominee.txtViewSl.text =
            getString(R.string.three)

        binding.appBarAccount.accountContent.introducer.textViewTitle.text =
            getString(R.string.introducer)
        binding.appBarAccount.accountContent.introducer.txtViewSl.text =
            getString(R.string.four)

        binding.appBarAccount.accountContent.transactionProfile.textViewTitle.text =
            getString(R.string.transaction_profile)
        binding.appBarAccount.accountContent.transactionProfile.txtViewSl.text =
            getString(R.string.five)

        binding.appBarAccount.accountContent.review.textViewTitle.text =
            getString(R.string.review)
        binding.appBarAccount.accountContent.review.txtViewSl.text =
            getString(R.string.six)
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