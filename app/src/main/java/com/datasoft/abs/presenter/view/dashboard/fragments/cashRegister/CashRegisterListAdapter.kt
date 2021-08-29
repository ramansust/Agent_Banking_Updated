package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.CashRegisterRowBinding
import javax.inject.Inject

class CashRegisterListAdapter @Inject constructor() :
    RecyclerView.Adapter<CashRegisterListAdapter.CashRegisterViewHolder>() {

    inner class CashRegisterViewHolder(val binding: CashRegisterRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashRegisterViewHolder {

        val binding = CashRegisterRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CashRegisterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: CashRegisterViewHolder, position: Int) {
        val customer = differ.currentList[position]

        with(customer) {
            holder.binding.txtViewSlNo.text = this.id.toString()
            holder.binding.txtViewDate.text = this.entryDate
            holder.binding.txtViewDebit.text = this.senderAccNumber
            holder.binding.txtViewCredit.text = this.receiverAccNumber
            holder.binding.txtViewBalance.text = this.receiverRouting
            holder.binding.txtViewEntryBy.text = this.entryDate
            holder.binding.txtViewRemarks.text = this.senderAccNumber
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(customer) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }
}













