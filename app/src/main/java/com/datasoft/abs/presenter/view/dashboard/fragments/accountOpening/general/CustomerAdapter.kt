package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.createAccount.general.CustomerData
import com.datasoft.abs.databinding.CustomerListRowBinding
import javax.inject.Inject

class CustomerAdapter @Inject constructor() :
    RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    inner class CustomerViewHolder(val binding: CustomerListRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CustomerData>() {
        override fun areItemsTheSame(
            oldItem: CustomerData,
            newItem: CustomerData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CustomerData,
            newItem: CustomerData
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val binding = CustomerListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewName.text = value.fullName
        holder.binding.txtViewFatherName.text = value.fatherName
        holder.binding.txtViewMotherName.text = value.motherName
        holder.binding.txtViewDob.text = value.dob

        if(differ.currentList.size > 1) {
            holder.binding.switchSignatory.isChecked = value.isSignatory
            holder.binding.switchMandatory.isChecked = value.isRequired
        } else {
            holder.binding.switchSignatory.isEnabled = false
            holder.binding.switchMandatory.isEnabled = false
        }
    }
}













