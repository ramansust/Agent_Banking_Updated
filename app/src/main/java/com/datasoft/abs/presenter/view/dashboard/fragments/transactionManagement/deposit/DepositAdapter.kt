package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.deposit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.Row
import com.datasoft.abs.databinding.AccountRowBinding
import javax.inject.Inject

class DepositAdapter @Inject constructor() :
    RecyclerView.Adapter<DepositAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: AccountRowBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val binding = AccountRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            holder.binding.txtViewAccountTitle.text = this.transactionNo
            holder.binding.txtViewAccountNumber.text = this.crAccountNumber
            holder.binding.txtViewSourceOfFund.text = this.transactionDate
            holder.binding.txtViewUnit.text = this.drAccountNumber

//            glide.load(this.imageUrl).into(holder.binding.ivAutoImageSlider)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(account) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













