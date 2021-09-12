package com.datasoft.abs.presenter.view.dashboard.fragments.customerList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.customerList.Row
import com.datasoft.abs.databinding.CustomerRowBinding
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT_API
import com.datasoft.abs.presenter.utils.Status.ACTIVE
import com.datasoft.abs.presenter.utils.Status.DRAFT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CustomerListAdapter @Inject constructor() :
    RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder>() {

    inner class CustomerViewHolder(val binding: CustomerRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem.customerNo == newItem.customerNo
        }

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
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

    private var onEditClickListener: ((Row) -> Unit)? = null
    private var onDetailClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = differ.currentList[position]

        with(customer) {
            holder.binding.txtViewSlNo.text = this.id.toString()
            holder.binding.txtViewCustomerNo.text = this.customerNo
            holder.binding.txtViewName.text = this.fullName
            holder.binding.txtViewCustomerType.text = this.customerType
            holder.binding.txtViewUnit.text = this.branchName
            try {
                holder.binding.txtViewEntryDate.text = SimpleDateFormat(
                    DATE_FORMAT,
                    Locale.US
                ).format(SimpleDateFormat(DATE_FORMAT_API, Locale.US).parse(this.entryDate))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            when (this.customerStatus) {
                DRAFT.type -> {
                    holder.binding.imgViewDetails.visibility = View.GONE
                }
                ACTIVE.type -> {
                    holder.binding.imgViewEdit.visibility = View.GONE
                }
                else -> {
                    holder.binding.imgViewDetails.visibility = View.VISIBLE
                    holder.binding.imgViewEdit.visibility = View.VISIBLE
                }
            }
        }

        holder.binding.imgViewDetails.setOnClickListener {
            onDetailClickListener?.let { it(customer) }
        }

        holder.binding.imgViewEdit.setOnClickListener {
            onEditClickListener?.let { it(customer) }
        }
    }

    fun setOnEditClickListener(listener: (Row) -> Unit) {
        onEditClickListener = listener
    }

    fun setOnDetailClickListener(listener: (Row) -> Unit) {
        onDetailClickListener = listener
    }

}













