package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.transactionProfile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.config.TransactionProfileConfig
import com.datasoft.abs.databinding.AddressRowBinding
import javax.inject.Inject

class TransactionProfileAdapter @Inject constructor() :
    RecyclerView.Adapter<TransactionProfileAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TransactionProfileConfig>() {
        override fun areItemsTheSame(oldItem: TransactionProfileConfig, newItem: TransactionProfileConfig): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TransactionProfileConfig, newItem: TransactionProfileConfig): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {

        val binding = AddressRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewAddress.text = value.transactionProfileList[0].name
    }
}













