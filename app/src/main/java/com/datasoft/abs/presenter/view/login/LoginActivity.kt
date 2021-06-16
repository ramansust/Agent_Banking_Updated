package com.datasoft.abs.presenter.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.databinding.ActivityLoginBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
import com.datasoft.abs.presenter.view.dashboard.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var toastHelper: ToastHelper

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
        toastHelper.toastMessages.startListening {
            showToast(it)
        }

        loginViewModel.getLoginData().observe(this, { response ->
            when(response) {
                is Resource.Success -> {
                    goneProgressBar()
                    response.data?.let { loginResponse ->
                        navigateToMainScreen(loginResponse)
                    }
                }
                is Resource.Error -> {
                    goneProgressBar()
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun doLogin() {
        loginViewModel.requestLogin(
            binding.username.text.trim().toString(),
            binding.password.text.toString()
        )
    }

    private fun navigateToMainScreen(loginResponse: LoginResponse) {
        val nextScreenIntent = Intent(this, MainActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }

    private fun goneProgressBar() {
        binding.loaderView.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loaderView.visibility = View.VISIBLE
    }
}
