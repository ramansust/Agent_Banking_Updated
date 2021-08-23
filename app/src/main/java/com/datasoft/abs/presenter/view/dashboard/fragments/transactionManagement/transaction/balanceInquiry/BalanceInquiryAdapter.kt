package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.BalanceInquiryResponseItem
import com.datasoft.abs.databinding.BalanceInquiryRowBinding
import javax.inject.Inject

class BalanceInquiryAdapter @Inject constructor() :
    RecyclerView.Adapter<BalanceInquiryAdapter.BalanceInquiryViewHolder>() {

    inner class BalanceInquiryViewHolder(val binding: BalanceInquiryRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<BalanceInquiryResponseItem>() {
        override fun areItemsTheSame(oldItem: BalanceInquiryResponseItem, newItem: BalanceInquiryResponseItem): Boolean {
            return oldItem.transactionNo == newItem.transactionNo
        }

        override fun areContentsTheSame(oldItem: BalanceInquiryResponseItem, newItem: BalanceInquiryResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceInquiryViewHolder {

        val binding = BalanceInquiryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BalanceInquiryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BalanceInquiryViewHolder, position: Int) {
        val account = differ.currentList[position]

        with(account) {
            holder.binding.txtViewTransactionNo.text = this.transactionNo
            holder.binding.txtViewDebit.text = this.debit.toString()
            holder.binding.txtViewCredit.text = this.credit.toString()
            holder.binding.txtViewDate.text = this.transactionDate
        }
    }

}













