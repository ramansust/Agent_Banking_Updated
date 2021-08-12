package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.config.TpDetail
import com.datasoft.abs.databinding.TransactionRowBinding
import javax.inject.Inject

class TransactionAdapter @Inject constructor() :
    RecyclerView.Adapter<TransactionAdapter.TransactionProfileViewModel>() {

    inner class TransactionProfileViewModel(val binding: TransactionRowBinding) :
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
            TransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionProfileViewModel(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TransactionProfileViewModel, position: Int) {
        val value = differ.currentList[position]

        val name = value.profileName + " " + value.codeName
        holder.binding.txtViewTransactionType.text = name
        holder.binding.txtViewMonthlyTransaction.text = value.limitNoOfMonthlyTrn.toString()
        holder.binding.txtViewMaxTransactionAmount.text = value.limitMaxTrnAmt.toString()
    }
}













