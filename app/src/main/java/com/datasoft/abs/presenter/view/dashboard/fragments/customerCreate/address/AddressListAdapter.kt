package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.databinding.AddressRowBinding
import javax.inject.Inject

class AddressListAdapter @Inject constructor() :
    RecyclerView.Adapter<AddressListAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
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

    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val value = differ.currentList[position]

        with(value) {
            holder.binding.txtViewAddress.text = value
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(value) }
        }
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}













