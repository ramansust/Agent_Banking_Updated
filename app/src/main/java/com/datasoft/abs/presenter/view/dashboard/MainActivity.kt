package com.datasoft.abs.presenter.view.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityMainBinding
import com.datasoft.abs.presenter.utils.Constant.USER_NAME
import com.datasoft.abs.presenter.view.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var glide: RequestManager

    private var mainMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile,
                R.id.nav_dashboard,
                R.id.nav_customer,
                R.id.nav_customer_list,
                R.id.nav_account,
                R.id.nav_account_list,
                R.id.nav_transaction,
                R.id.nav_business_close,
                R.id.nav_cash_register
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            binding.appBarMain.txtViewTitle.text = when (destination.id) {
                R.id.nav_profile -> getString(R.string.menu_profile)
                R.id.nav_dashboard -> getString(R.string.menu_dashboard)
                R.id.nav_customer -> getString(R.string.menu_customer)
                R.id.nav_customer_list -> getString(R.string.menu_customer_list)
                R.id.nav_account -> getString(R.string.menu_account)
                R.id.nav_account_list -> getString(R.string.menu_account_list)
                R.id.nav_transaction -> getString(R.string.menu_transaction)
                R.id.nav_business_close -> getString(R.string.menu_business_close)
                R.id.nav_cash_register -> getString(R.string.menu_cash_register)
                R.id.transaction -> getString(R.string.menu_transaction)
                R.id.disbursement -> getString(R.string.transaction_disbursement)
                R.id.deposit -> getString(R.string.deposit_list)
                R.id.withdraw -> getString(R.string.withdrawal_list)
                R.id.balance -> getString(R.string.balance_transfer_list)
                R.id.eftn, R.id.EFTNTransaction -> getString(R.string.eftn_transaction)
                R.id.rtgs, R.id.RTGSTransaction -> getString(R.string.rtgs_transaction)
                R.id.transactionDetailsFragment, R.id.EFTNTransactionDetailsFragment -> getString(R.string.transaction_details)
                R.id.changePasswordFragment -> getString(R.string.change_password)
                R.id.cashRegisterCreateFragment -> getString(R.string.cash_breakdown)
                R.id.feederTransactionFragment, R.id.feederTransactionCreateFragment -> getString(R.string.feeder_transaction)
                else -> getString(R.string.menu_profile)
            }

            when (destination.id) {
                R.id.transaction, R.id.disbursement, R.id.deposit, R.id.withdraw, R.id.balance, R.id.eftn, R.id.EFTNTransaction, R.id.rtgs, R.id.RTGSTransaction, R.id.transactionDetailsFragment, R.id.EFTNTransactionDetailsFragment, R.id.changePasswordFragment, R.id.cashRegisterCreateFragment, R.id.feederTransactionFragment, R.id.feederTransactionCreateFragment -> hideToolbarItems()
                else -> showToolbarItems()
            }
        }

        setDataIntoNavHeader()
        setDataIntoToolbar()
    }

    private fun setDataIntoToolbar() {
        binding.appBarMain.txtViewUserName.text = intent.extras?.get(USER_NAME).toString()
    }

    private fun hideToolbarItems() {
        mainMenu?.findItem(R.id.action_logout)?.isVisible = false
        binding.appBarMain.txtViewUserName.visibility = View.GONE
        binding.appBarMain.imgViewProfile.visibility = View.GONE
        binding.appBarMain.view.visibility = View.GONE
        binding.appBarMain.imgViewNotifications.visibility = View.GONE
    }

    private fun showToolbarItems() {
        mainMenu?.findItem(R.id.action_logout)?.isVisible = true
        binding.appBarMain.txtViewUserName.visibility = View.VISIBLE
        binding.appBarMain.imgViewProfile.visibility = View.VISIBLE
        binding.appBarMain.view.visibility = View.VISIBLE
        binding.appBarMain.imgViewNotifications.visibility = View.VISIBLE

        glide.load(R.drawable.ic_user_photo).apply(RequestOptions().circleCrop())
            .into(binding.appBarMain.imgViewProfile)
    }

    private fun setDataIntoNavHeader() {
        val headerView: View = binding.navView.getHeaderView(0)
//        headerView.findViewById<TextView>(R.id.txt_view_name).text = "Android"
//        headerView.findViewById<TextView>(R.id.txt_view_email).text = "android.studio@gmail.com"

        glide.load(R.drawable.ic_user_photo).apply(RequestOptions().circleCrop())
            .into(headerView.findViewById(R.id.img_view_user_profile))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        mainMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(resources.getString(R.string.confirmation_message_logout))
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}