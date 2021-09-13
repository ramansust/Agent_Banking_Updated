package com.datasoft.abs.presenter.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.datasoft.abs.data.dto.login.LoginResponse
import com.datasoft.abs.databinding.ActivityLoginBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Status
import com.datasoft.abs.presenter.utils.Constant.USER_NAME
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
        toastHelper.toastMessages.startListening { response ->
            response.getContentIfNotHandled()?.let {
                showToast(it)
            }
        }

        loginViewModel.getLoginData().observe(this, { response ->

            response?.getContentIfNotHandled()?.let { result ->

                when (result.status) {
                    Status.SUCCESS -> {
                        goneProgressBar()
                        result.data?.let { loginResponse ->
                            navigateToMainScreen(loginResponse)
                        }
                    }
                    Status.ERROR -> {
                        goneProgressBar()
                        result.message?.let { message ->
                            toastHelper.sendToast(message)
                            Log.e("TAG", "An error occurred: $message")
                        }
                    }
                    Status.LOADING -> {
                        showProgressBar()
                    }
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
        val nextScreenIntent = Intent(this, MainActivity::class.java).putExtra(USER_NAME, loginResponse.userName)
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
