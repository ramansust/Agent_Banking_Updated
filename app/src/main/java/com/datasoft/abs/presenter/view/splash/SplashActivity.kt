package com.datasoft.abs.presenter.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.datasoft.abs.databinding.ActivitySplashBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.utils.Constant.SPLASH_DELAY
import com.datasoft.abs.presenter.view.dashboard.fragments.customer.CustomerActivity
import com.datasoft.abs.presenter.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToLoginScreen()
    }

    private fun navigateToLoginScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val nextScreenIntent = Intent(this, CustomerActivity::class.java)
            startActivity(nextScreenIntent)
            finish()
        }, SPLASH_DELAY.toLong())
    }
}