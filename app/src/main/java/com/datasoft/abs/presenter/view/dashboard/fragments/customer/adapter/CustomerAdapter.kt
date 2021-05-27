package com.datasoft.abs.presenter.view.dashboard.fragments.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.customer.CustomerResponse
import com.datasoft.abs.databinding.CustomerRowBinding

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    inner class CustomerViewHolder(val binding: CustomerRowBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CustomerResponse>() {
        override fun areItemsTheSame(oldItem: CustomerResponse, newItem: CustomerResponse): Boolean {
            return oldItem.timeStamp == newItem.timeStamp
        }

        override fun areContentsTheSame(oldItem: CustomerResponse, newItem: CustomerResponse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val binding = CustomerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((CustomerResponse) -> Unit)? = null

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val article = differ.currentList[position]

        with(article) {
            holder.binding.textView1.text = this.responseData.contactPersonName
            holder.binding.textView2.text = this.responseData.accountBranchName
            holder.binding.textView3.text = this.responseData.accountConcentrationName
        }

        holder.itemView.setOnClickListener() {
            onItemClickListener?.let { it(article) }
        }
    }

    fun setOnItemClickListener(listener: (CustomerResponse) -> Unit) {
        onItemClickListener = listener
    }

}













