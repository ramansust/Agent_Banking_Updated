package com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.accountList.Row
import com.datasoft.abs.databinding.AccountRowBinding
import com.datasoft.abs.presenter.utils.Status
import javax.inject.Inject

class AccountAdapter @Inject constructor() :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: AccountRowBinding) :
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
            holder.binding.txtViewAccountTitle.text = this.accountTitle
            holder.binding.txtViewAccountNumber.text = this.accountNumber
            holder.binding.txtViewSourceOfFund.text = this.natureOfBusiness
            holder.binding.txtViewUnit.text = this.branchName

            when (this.accountStatus) {
                Status.DRAFT.type -> {
                    holder.binding.imgViewDetails.visibility = View.GONE
                }

                Status.ACTIVE.type -> {
                    holder.binding.imgViewEdit.visibility = View.GONE
                }

                else -> {
                    holder.binding.imgViewDetails.visibility = View.VISIBLE
                    holder.binding.imgViewEdit.visibility = View.VISIBLE
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(account) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













