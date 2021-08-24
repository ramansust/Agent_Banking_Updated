package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.datasoft.abs.databinding.FragmentCashRegisterCreateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashRegisterCreateFragment : Fragment() {

    private var _binding: FragmentCashRegisterCreateBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCashRegisterCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edTxtThousand.addTextChangedListener(textWatcherThousand)
        binding.edTxtFiveHundred.addTextChangedListener(textWatcherFiveHundred)
        binding.edTxtHundred.addTextChangedListener(textWatcherHundred)
        binding.edTxtFifty.addTextChangedListener(textWatcherFifty)
        binding.edTxtTwenty.addTextChangedListener(textWatcherTwenty)
        binding.edTxtTen.addTextChangedListener(textWatcherTen)
        binding.edTxtFive.addTextChangedListener(textWatcherFive)
        binding.edTxtTwo.addTextChangedListener(textWatcherTwo)
    }

    private val textWatcherThousand = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 1000 * charSequence.toString().toInt()
                binding.txtViewThousand.text = value.toString()
            } else {
                binding.txtViewThousand.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherFiveHundred = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 500 * charSequence.toString().toInt()
                binding.txtViewFiveHundred.text = value.toString()
            } else {
                binding.txtViewFiveHundred.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherHundred = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 100 * charSequence.toString().toInt()
                binding.txtViewHundred.text = value.toString()
            } else {
                binding.txtViewHundred.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherFifty = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 50 * charSequence.toString().toInt()
                binding.txtViewFifty.text = value.toString()
            } else {
                binding.txtViewFifty.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherTwenty = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 20 * charSequence.toString().toInt()
                binding.txtViewTwenty.text = value.toString()
            } else {
                binding.txtViewTwenty.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherTen = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 10 * charSequence.toString().toInt()
                binding.txtViewTen.text = value.toString()
            } else {
                binding.txtViewTen.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherFive = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 5 * charSequence.toString().toInt()
                binding.txtViewFive.text = value.toString()
            } else {
                binding.txtViewFive.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private val textWatcherTwo = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            if (charSequence.isNotEmpty()) {
                val value = 2 * charSequence.toString().toInt()
                binding.txtViewTwo.text = value.toString()
            } else {
                binding.txtViewTwo.text = "0"
            }

            displayTotal()
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun displayTotal() {
        val totalAmount: Long = (binding.txtViewThousand.text.toString()
            .toInt() + binding.txtViewFiveHundred.text.toString()
            .toInt() + binding.txtViewHundred.text.toString()
            .toInt() + binding.txtViewFifty.text.toString()
            .toInt() + binding.txtViewTwenty.text.toString()
            .toInt() + binding.txtViewTen.text.toString()
            .toInt() + binding.txtViewFive.text.toString()
            .toInt() + binding.txtViewTwo.text.toString().toInt()).toLong()

        binding.txtViewTotalAmount.text = totalAmount.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}