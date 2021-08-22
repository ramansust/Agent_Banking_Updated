package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.databinding.BalanceInquiryRowBinding
import javax.inject.Inject

class BalanceInquiryAdapter @Inject constructor() :
    RecyclerView.Adapter<BalanceInquiryAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: BalanceInquiryRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem.accountNumber == newItem.accountNumber
        }

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val binding = BalanceInquiryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = differ.currentList[position]

        with(account) {
            holder.binding.txtViewTransactionNo.text = this.id.toString()
            holder.binding.txtViewDebit.text = this.accountTitle
            holder.binding.txtViewCredit.text = this.accountNumber
            holder.binding.txtViewDate.text = this.natureOfBusiness
        }
    }

}













