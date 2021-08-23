package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.Row
import com.datasoft.abs.databinding.DepositRowBinding
import javax.inject.Inject

class DepositAdapter @Inject constructor() :
    RecyclerView.Adapter<DepositAdapter.DepositViewHolder>() {

    inner class DepositViewHolder(val binding: DepositRowBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositViewHolder {

        val binding = DepositRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepositViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        val deposit = differ.currentList[position]

        with(deposit) {
            holder.binding.txtViewSlNo.text = (position  + 1).toString()
            holder.binding.txtViewTransactionNo.text = this.transactionNo
            holder.binding.txtViewAccountNumber.text = this.crAccountNumber
            holder.binding.txtViewDateTime.text = this.transactionDate
            holder.binding.txtViewDescription.text = this.narration
            holder.binding.txtViewAmount.text = this.balance.toString()
        }

        holder.binding.imgViewEdit.setOnClickListener {
            onItemClickListener?.let { it(deposit) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













