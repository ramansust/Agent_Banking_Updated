package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import android.os.Bundle
import androidx.activity.viewModels
import com.datasoft.abs.databinding.AddressActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : BaseActivity() {

    private val addressViewModel: AddressViewModel by viewModels()
    private lateinit var binding: AddressActivityBinding

    override fun observeViewModel() {
    }

    override fun initViewBinding() {
        binding = AddressActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}