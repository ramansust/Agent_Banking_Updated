package com.datasoft.abs.presenter.view.dashboard.fragments.cashRegister.feederTransaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.FeederTransactionRowBinding
import javax.inject.Inject

class FeederTransactionListAdapter @Inject constructor() :
    RecyclerView.Adapter<FeederTransactionListAdapter.FeederViewHolder>() {

    inner class FeederViewHolder(val binding: FeederTransactionRowBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeederViewHolder {

        val binding = FeederTransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeederViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: FeederViewHolder, position: Int) {
        val customer = differ.currentList[position]

        with(customer) {
            holder.binding.txtViewSlNo.text = this.id.toString()
            holder.binding.txtViewDate.text = this.senderAccNumber
            holder.binding.txtViewAccountNumber.text = this.receiverRouting
            holder.binding.txtViewReferenceNumber.text = this.receiverAccNumber
            holder.binding.txtViewTransactionType.text = this.receiverName
            holder.binding.txtViewAmount.text = this.entryDate
            holder.binding.txtViewCurrentBalance.text = this.entryDate
            holder.binding.txtViewEntryBy.text = this.entryDate
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(customer) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













