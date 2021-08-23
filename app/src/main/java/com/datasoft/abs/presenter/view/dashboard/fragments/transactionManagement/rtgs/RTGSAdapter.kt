package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.rtgs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.databinding.EftnTransactionRowBinding
import javax.inject.Inject

class RTGSAdapter @Inject constructor() :
    RecyclerView.Adapter<RTGSAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: EftnTransactionRowBinding) :
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

        val binding = EftnTransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = differ.currentList[position]

        with(account) {
            holder.binding.txtViewSlNo.text = this.id.toString()
            holder.binding.txtViewSenderAc.text = this.accountTitle
            holder.binding.txtViewReceiverName.text = this.accountNumber
            holder.binding.txtViewReceiverAc.text = this.natureOfBusiness
            holder.binding.txtViewReceiverBank.text = this.natureOfBusiness
            holder.binding.txtViewRouting.text = this.natureOfBusiness
            holder.binding.txtViewAmount.text = this.natureOfBusiness
            holder.binding.txtViewEntryDate.text = this.natureOfBusiness
        }

        holder.binding.imgViewEdit.setOnClickListener {
            onItemClickListener?.let { it(account) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













