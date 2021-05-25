package com.datasoft.abs.presenter.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.datasoft.abs.databinding.ActivityLoginBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.view.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.login.setOnClickListener {
            binding.loaderView.visibility = View.VISIBLE
            doLogin()
        }
    }

    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun observeViewModel() {
        loginViewModel.getLoginStatus().observe(this, Observer {
            binding.loaderView.visibility = View.GONE
        })

        loginViewModel.getLoginMessage().observe(this, Observer {
            if (it == "Successfully Logged in")
                navigateToMainScreen()
        })
    }

    private fun doLogin() {
        loginViewModel.requestLogin(
            binding.username.text.trim().toString(),
            binding.password.text.toString()
        )
    }

    private fun navigateToMainScreen() {
        val nextScreenIntent = Intent(this, MainActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }
}
