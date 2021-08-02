package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

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

        val binding = TransactionProfileRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionProfileViewModel(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TransactionProfileViewModel, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewName.text = value.name

        holder.binding.edTxtDailyTransaction.setText(value.limitNoOfDailyTrn.toString())
        holder.binding.edTxtDailyAmount.setText(value.limitDailyTrnAmt.toString())
        holder.binding.edTxtMonthlyTransaction.setText(value.limitNoOfMonthlyTrn.toString())
        holder.binding.edTxtMonthlyAmount.setText(value.limitMonthlyTrnAmt.toString())
        holder.binding.edTxtTransactionAmount.setText(value.limitMaxTrnAmt.toString())
    }
}













