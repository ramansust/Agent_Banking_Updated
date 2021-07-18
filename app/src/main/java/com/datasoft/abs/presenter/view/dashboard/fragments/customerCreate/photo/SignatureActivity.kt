package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.datasoft.abs.databinding.ActivitySignatureBinding
import com.datasoft.abs.presenter.base.BaseActivity
import com.datasoft.abs.presenter.utils.Constant
import com.github.gcacace.signaturepad.views.SignaturePad
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class SignatureActivity : BaseActivity() {

    private lateinit var binding: ActivitySignatureBinding

    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        binding = ActivitySignatureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnCross.setOnClickListener {
            finish()
        }

        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {

            override fun onStartSigning() {

            }

            override fun onSigned() {
                binding.btnSave.isEnabled = true
                binding.btnClear.isEnabled = true
            }

            override fun onClear() {
                binding.btnSave.isEnabled = false
                binding.btnClear.isEnabled = false
            }
        })

        binding.btnClear.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.btnSave.setOnClickListener {
            val signatureBitmap: Bitmap = binding.signaturePad.signatureBitmap

            val stream = ByteArrayOutputStream()
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()

            val data = Intent()
            data.putExtra(Constant.SIGNATURE_INFO, byteArray)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}