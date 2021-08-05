package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.RequestManager
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.databinding.NomineeActivityBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant
import com.datasoft.abs.presenter.utils.Constant.ADULT_AGE
import com.datasoft.abs.presenter.utils.Constant.IMAGE_COMPRESS
import com.datasoft.abs.presenter.utils.Constant.IMAGE_RESOLUTION_HEIGHT
import com.datasoft.abs.presenter.utils.Constant.IMAGE_RESOLUTION_WIDTH
import com.datasoft.abs.presenter.utils.Constant.NOMINEE_INFO
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo.SignatureActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pixelcarrot.base64image.Base64Image
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NomineeActivity : BaseActivity() {

    private val myCalendar: Calendar = Calendar.getInstance()

    private val accountViewModel: AccountViewModel by viewModels()
    private val nomineeViewModel: NomineeViewModel by viewModels()
    private lateinit var binding: NomineeActivityBinding

    private val relationList = mutableListOf<CommonModel>()
    private val documentList = mutableListOf<CommonModel>()
    private val occupationList = mutableListOf<CommonModel>()

    @Inject
    lateinit var glide: RequestManager
    private var isMinor = false

    override fun observeViewModel() {

        nomineeViewModel.getBackImage().observe(this, {
            if (!it) {
                binding.txtViewBackDoc.visibility = View.INVISIBLE
                binding.imgViewBackDoc.visibility = View.INVISIBLE
                binding.btnTakeBackDoc.visibility = View.INVISIBLE
                binding.btnBrowseBackDoc.visibility = View.INVISIBLE
            } else {
                binding.txtViewBackDoc.visibility = View.VISIBLE
                binding.imgViewBackDoc.visibility = View.VISIBLE
                binding.btnTakeBackDoc.visibility = View.VISIBLE
                binding.btnBrowseBackDoc.visibility = View.VISIBLE
            }
        })

        nomineeViewModel.getBackImageNominee().observe(this, {
            if (!it) {
                binding.txtViewNomineeBackDoc.visibility = View.INVISIBLE
                binding.imgViewNomineeBackDoc.visibility = View.INVISIBLE
                binding.btnTakeNomineeBackDoc.visibility = View.INVISIBLE
                binding.btnBrowseNomineeBackDoc.visibility = View.INVISIBLE
            } else {
                binding.txtViewNomineeBackDoc.visibility = View.VISIBLE
                binding.imgViewNomineeBackDoc.visibility = View.VISIBLE
                binding.btnTakeNomineeBackDoc.visibility = View.VISIBLE
                binding.btnBrowseNomineeBackDoc.visibility = View.VISIBLE
            }
        })

        nomineeViewModel.getNomineeAge().observe(this, {
            if (it < ADULT_AGE) {
                isMinor = true
                binding.linearLayoutNominee1.visibility = View.VISIBLE
                binding.linearLayoutNominee2.visibility = View.VISIBLE
            } else {
                isMinor = false
                binding.linearLayoutNominee1.visibility = View.GONE
                binding.linearLayoutNominee2.visibility = View.GONE
            }
        })

        accountViewModel.getConfigData().observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        relationList.addAll(it.relationList)
                        binding.spinnerRelation.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, relationList)

                        binding.spinnerNomineeWithRelation.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, relationList)

                        documentList.addAll(it.documentTypeList)
                        binding.spinnerDocumentsType.adapter =
                            ArrayAdapter(this, android.R.layout.simple_spinner_item, documentList)

                        binding.spinnerNomineeDocType.adapter =
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
                binding.edTxtNomineeName.text.trim().toString(),
                binding.edTxtNomineeDob.text.trim().toString(),
                binding.edTxtNomineePermanentAddress.text.trim().toString(),
                if (documentList.isNotEmpty()) documentList[binding.spinnerNomineeDocType.selectedItemPosition].id else 0,
                binding.edTxtNomineeExpiryDate.text.trim().toString(),
                binding.edTxtNomineeFatherSpouseName.text.trim().toString(),
                binding.edTxtNomineePresentAddress.text.trim().toString(),
                if (relationList.isNotEmpty()) relationList[binding.spinnerNomineeWithRelation.selectedItemPosition].id else 0,
                binding.edTxtNomineeIdValue.text.trim().toString(),
                isMinor
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

        binding.edTxtNomineeExpiryDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, nomineeDateExpiry, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        binding.edTxtNomineeDob.setOnClickListener {
            val datePicker = DatePickerDialog(
                this, nomineeBirthDate, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )

            val guardianCalender = Calendar.getInstance()
            guardianCalender.set(
                guardianCalender[Calendar.YEAR] - ADULT_AGE,
                guardianCalender[Calendar.MONDAY],
                guardianCalender[Calendar.DAY_OF_MONTH]
            )

            datePicker.datePicker.maxDate = guardianCalender.timeInMillis
            datePicker.show()
        }

        binding.btnTakeNomineePhoto.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(IMAGE_RESOLUTION_WIDTH, IMAGE_RESOLUTION_HEIGHT)
                .createIntent { intent ->
                    startForNomineePhotoResult.launch(intent)
                }

        }

        binding.btnBrowseNomineePhoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(IMAGE_RESOLUTION_WIDTH, IMAGE_RESOLUTION_HEIGHT)
                .createIntent { intent ->
                    startForNomineePhotoResult.launch(intent)
                }

        }

        binding.btnTakeFrontDoc.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForDocFrontResult.launch(intent)
                }

        }

        binding.btnBrowseFrontDoc.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForDocFrontResult.launch(intent)
                }

        }

        binding.btnTakeBackDoc.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForDocBackResult.launch(intent)
                }

        }

        binding.btnBrowseBackDoc.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForDocBackResult.launch(intent)
                }

        }

        binding.btnBrowseNomineeSignature.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(IMAGE_RESOLUTION_WIDTH, IMAGE_RESOLUTION_HEIGHT)
                .createIntent { intent ->
                    startForNomineeSignatureResult.launch(intent)
                }

        }

        binding.btnTakeNomineeSignature.setOnClickListener {
            resultLauncherNomineeSignature.launch(
                Intent(
                    this,
                    SignatureActivity::class.java
                )
            )
        }

        binding.btnTakeNomineeFrontDoc.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForNomineeDocFrontResult.launch(intent)
                }

        }

        binding.btnBrowseNomineeFrontDoc.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForNomineeDocFrontResult.launch(intent)
                }

        }

        binding.btnTakeNomineeBackDoc.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForNomineeDocBackResult.launch(intent)
                }

        }

        binding.btnBrowseNomineeBackDoc.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(IMAGE_COMPRESS)
                .maxResultSize(
                    IMAGE_RESOLUTION_WIDTH,
                    IMAGE_RESOLUTION_HEIGHT
                )
                .createIntent { intent ->
                    startForNomineeDocBackResult.launch(intent)
                }

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
                    nomineeViewModel.setBackImage(false)
                }
            }

        binding.spinnerNomineeDocType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    nomineeViewModel.setBackImageNominee(documentList[position].isBackRequired)
                    nomineeViewModel.setBackImageNominee(false)
                }
            }

    }

    private var resultLauncherNomineeSignature =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val byteArray = result.data?.getByteArrayExtra(Constant.SIGNATURE_INFO)

                byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }?.let {

                    Base64Image.encode(it) { base64 ->
                        base64?.let { value ->
                            glide.load(Base64.decode(value, Base64.DEFAULT))
                                .into(binding.imgViewNomineeSignature)
                        }
                    }
                }
            }
        }

    private val startForNomineeSignatureResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewNomineeSignature.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForNomineePhotoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewNomineePhoto.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForDocFrontResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewFrontDoc.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForDocBackResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewBackDoc.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForNomineeDocFrontResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewNomineeFrontDoc.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForNomineeDocBackResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding.imgViewNomineeBackDoc.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
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

    private var nomineeDateExpiry =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateNomineeExpiry()
        }

    private fun updateNomineeExpiry() {
        val sdf = SimpleDateFormat(Constant.DATE_FORMAT, Locale.US)
        binding.edTxtNomineeExpiryDate.setText(sdf.format(myCalendar.time))
    }

    private var nomineeBirthDate =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateNomineeBirthDate()
        }

    private fun updateNomineeBirthDate() {
        val sdf = SimpleDateFormat(Constant.DATE_FORMAT, Locale.US)
        binding.edTxtNomineeDob.setText(sdf.format(myCalendar.time))
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}