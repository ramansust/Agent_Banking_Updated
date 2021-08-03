package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.config.TpDetail
import com.datasoft.abs.databinding.TransactionProfileRowBinding
import javax.inject.Inject

class TransactionProfileAdapter @Inject constructor() :
    RecyclerView.Adapter<TransactionProfileAdapter.TransactionProfileViewModel>() {

    inner class TransactionProfileViewModel(val binding: TransactionProfileRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TpDetail>() {
        override fun areItemsTheSame(oldItem: TpDetail, newItem: TpDetail): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TpDetail, newItem: TpDetail): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionProfileViewModel {

        val binding =
            TransactionProfileRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionProfileViewModel(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onTextChangeListener: ((Int, Int, Int) -> Unit)? = null

    override fun onBindViewHolder(holder: TransactionProfileViewModel, position: Int) {
        val value = differ.currentList[position]

        val name = value.profileName + " " + value.codeName
        holder.binding.txtViewName.text = name

        holder.binding.edTxtDailyTransaction.setText(value.limitNoOfDailyTrn.toString())
        holder.binding.edTxtDailyAmount.setText(value.limitDailyTrnAmt.toString())
        holder.binding.edTxtMonthlyTransaction.setText(value.limitNoOfMonthlyTrn.toString())
        holder.binding.edTxtMonthlyAmount.setText(value.limitMonthlyTrnAmt.toString())
        holder.binding.edTxtTransactionAmount.setText(value.limitMaxTrnAmt.toString())

        holder.binding.edTxtDailyTransaction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeListener?.let {
                    it(
                        position,
                        if (charSequence.toString().isNotEmpty()) charSequence.toString()
                            .toInt() else 0,
                        0
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        holder.binding.edTxtDailyAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeListener?.let {
                    it(
                        position,
                        if (charSequence.toString().isNotEmpty()) charSequence.toString()
                            .toInt() else 0,
                        1
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        holder.binding.edTxtMonthlyTransaction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeListener?.let {
                    it(
                        position,
                        if (charSequence.toString().isNotEmpty()) charSequence.toString()
                            .toInt() else 0,
                        2
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        holder.binding.edTxtMonthlyAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeListener?.let {
                    it(
                        position,
                        if (charSequence.toString().isNotEmpty()) charSequence.toString()
                            .toInt() else 0,
                        3
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        holder.binding.edTxtTransactionAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                onTextChangeListener?.let {
                    it(
                        position,
                        if (charSequence.toString().isNotEmpty()) charSequence.toString()
                            .toInt() else 0,
                        4
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    fun setOnTextChangeListener(listener: (Int, Int, Int) -> Unit) {
        onTextChangeListener = listener
    }
}













