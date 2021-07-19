package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.datasoft.abs.data.dto.config.DocumentConfigData
import com.datasoft.abs.databinding.DocumentsActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.CustomerViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DocumentActivity : BaseActivity() {

    private val documentsViewModel: DocumentsViewModel by viewModels()
    private val customerViewModel: CustomerViewModel by viewModels()
    private lateinit var binding: DocumentsActivityBinding

    private val documentList = mutableListOf<DocumentConfigData>()
    private val myCalendar: Calendar = Calendar.getInstance()

    private var frontImageUri: String = ""
    private var backImageUri: String = ""

    override fun observeViewModel() {

        customerViewModel.getConfigData().observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        documentList.addAll(it.documentConfigData)
                        binding.spinnerDocumentsType.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, documentList)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        documentsViewModel.getBackImage().observe(this, {
            if (!it) {
                binding.txtViewBack.visibility = View.INVISIBLE
                binding.imgViewBack.visibility = View.INVISIBLE
                binding.btnTakeBack.visibility = View.INVISIBLE
                binding.btnBrowseBack.visibility = View.INVISIBLE
            } else {
                binding.txtViewBack.visibility = View.VISIBLE
                binding.imgViewBack.visibility = View.VISIBLE
                binding.btnTakeBack.visibility = View.VISIBLE
                binding.btnBrowseBack.visibility = View.VISIBLE
            }
        })

        documentsViewModel.getMessage().observe(this, {
            when (it) {
                is Resource.Success -> {
                    val data = Intent()
                    data.putExtra(Constant.DOCUMENT_INFO, it.data)
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
        binding = DocumentsActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customerViewModel.configData()

        binding.btnCross.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            documentsViewModel.checkData(
                if (documentList.isNotEmpty()) documentList[binding.spinnerDocumentsType.selectedItemPosition].id else 0,
                if (documentList.isNotEmpty()) documentList[binding.spinnerDocumentsType.selectedItemPosition].name else "",
                binding.edTxtIssueDate.text.trim().toString(),
                binding.edTxtDocumentId.text.trim().toString(),
                binding.edTxtExpiryDate.text.trim().toString(),
                binding.edTxtDescription.text.trim().toString(),
                frontImageUri,
                backImageUri
            )
        }

        binding.spinnerDocumentsType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    documentsViewModel.setBackImage(documentList[position].isBackRequired)
                }
            }

        binding.edTxtIssueDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, dateIssue, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
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

        binding.btnTakeFront.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForFrontResult.launch(intent)
                }

        }

        binding.btnBrowseFront.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForFrontResult.launch(intent)
                }

        }

        binding.btnTakeBack.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForBackResult.launch(intent)
                }

        }

        binding.btnBrowseBack.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForBackResult.launch(intent)
                }

        }
    }

    private var dateIssue = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateIssueDate()
    }

    private fun updateIssueDate() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        binding.edTxtIssueDate.setText(sdf.format(myCalendar.time))
    }

    private var dateExpiry =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateExpiryDate()
        }

    private fun updateExpiryDate() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        binding.edTxtExpiryDate.setText(sdf.format(myCalendar.time))
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }

    private val startForFrontResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    frontImageUri = fileUri.toString()
                    binding.imgViewFront.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForBackResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!

                    backImageUri = fileUri.toString()
                    binding.imgViewBack.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
}