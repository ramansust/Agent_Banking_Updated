package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.app.Activity
import android.app.DatePickerDialog
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
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.NOMINEE_INFO
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NomineeActivity : BaseActivity() {

    private val myCalendar: Calendar = Calendar.getInstance()

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
            nomineeViewModel.checkData(
                binding.edTxtFullName.text.trim().toString(),
                binding.edTxtMotherName.text.trim().toString(),
                binding.edTxtSpouseName.text.trim().toString(),
                if (relationList.isNotEmpty()) relationList[binding.spinnerRelation.selectedItemPosition].id else 0,
                if (documentList.isNotEmpty()) documentList[binding.spinnerDocumentsType.selectedItemPosition].id else 0,
                binding.edTxtExpiryDate.text.trim().toString(),
                binding.edTxtPresentAddress.text.trim().toString(),
                binding.edTxtFatherName.text.trim().toString(),
                binding.edTxtDob.text.trim().toString(),
                if (binding.edTxtPercentShare.text.trim().toString()
                        .isNotEmpty()
                ) binding.edTxtPercentShare.text.trim().toString()
                    .toInt() else 0,
                if (occupationList.isNotEmpty()) occupationList[binding.spinnerOccupation.selectedItemPosition].id else 0,
                binding.edTxtIdValue.text.trim().toString(),
                binding.edTxtPresentAddress.text.trim().toString(),
                "Applicant 1",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "",
                0,
                "",
            )
        }

        binding.edTxtExpiryDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, dateExpiry, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        binding.edTxtDob.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, birthDate, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }
    }

    private var dateExpiry =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateExpiry()
        }

    private fun updateExpiry() {
        val sdf = SimpleDateFormat(Constant.DATE_FORMAT, Locale.US)
        binding.edTxtExpiryDate.setText(sdf.format(myCalendar.time))
    }

    private var birthDate = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateBirthDate()
    }

    private fun updateBirthDate() {
        val sdf = SimpleDateFormat(Constant.DATE_FORMAT, Locale.US)
        binding.edTxtDob.setText(sdf.format(myCalendar.time))
        nomineeViewModel.nomineeAge(binding.edTxtDob.text.trim().toString())
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}