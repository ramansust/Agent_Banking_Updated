package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.NomineeActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.NOMINEE_INFO
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MinorActivity : BaseActivity() {

    private val accountViewModel: AccountViewModel by viewModels()
    private val nomineeViewModel: NomineeViewModel by viewModels()
    private lateinit var binding: NomineeActivityBinding

    private val relationList = mutableListOf<CommonModel>()
    private val documentList = mutableListOf<CommonModel>()
    private val occupationList = mutableListOf<CommonModel>()

    override fun observeViewModel() {

        accountViewModel.getConfigData().observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        relationList.addAll(it.relationList)
                        binding.spinnerRelation.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, relationList)

                        documentList.addAll(it.documentTypeList)
                        binding.spinnerDocumentsType.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, documentList)

                        occupationList.addAll(it.occupationList)
                        binding.spinnerOccupation.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, occupationList)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        binding.btnSave.isFocusable = false
                        binding.btnSave.isClickable = false
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        nomineeViewModel.getNomineeData().observe(this, {
            when (it) {
                is Resource.Success -> {
                    val data = Intent()
                    data.putExtra(NOMINEE_INFO, it.data)
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
        binding = NomineeActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountViewModel.accountConfigData()

        binding.btnCross.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
//            nomineeViewModel.checkData(
//
//            )
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}