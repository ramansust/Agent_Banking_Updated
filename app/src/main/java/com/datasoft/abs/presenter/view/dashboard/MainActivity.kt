package com.datasoft.abs.presenter.view.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.datasoft.abs.R
import com.datasoft.abs.databinding.ActivityMainBinding
import com.datasoft.abs.presenter.utils.Constant.USER_NAME
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)

//        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_menu)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_dashboard, R.id.nav_customer, R.id.nav_account, R.id.nav_transaction
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        setDataIntoNavHeader()
        setDataIntoToolbar()
    }

    private fun setDataIntoToolbar() {
        binding.appBarMain.txtViewUserName.text = intent.extras?.get(USER_NAME).toString()
    }

    private fun setDataIntoNavHeader() {
        val headerView: View = binding.navView.getHeaderView(0)
//        headerView.findViewById<TextView>(R.id.txt_view_name).text = "Android"
//        headerView.findViewById<TextView>(R.id.txt_view_email).text = "android.studio@gmail.com"

        glide.load("url")
            .into(headerView.findViewById(R.id.img_view_user_profile))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}