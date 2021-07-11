package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.datasoft.abs.databinding.AddressActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : BaseActivity() {

    private val addressViewModel: AddressViewModel by viewModels()
    private lateinit var binding: AddressActivityBinding

    override fun observeViewModel() {
        addressViewModel.getMessage().observe(this, {
            when (it) {
                is Resource.Success -> {
                    val data = Intent()
                    data.putExtra("data", it.data.toString())
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }

                is Resource.Error -> {
                    it.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {

                }
            }
        })
    }

    override fun initViewBinding() {
        binding = AddressActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnCross.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            addressViewModel.checkData(binding.edTxtFlatNo.text.trim().toString())
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}