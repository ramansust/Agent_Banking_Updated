package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.rtgs.Row
import com.datasoft.abs.databinding.EftnTransactionRowBinding
import com.datasoft.abs.presenter.utils.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EFTNListAdapter @Inject constructor() :
    RecyclerView.Adapter<EFTNListAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val binding: EftnTransactionRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem.receiverAccNumber == newItem.receiverAccNumber
        }

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {

        val binding =
            EftnTransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Row) -> Unit)? = null

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = differ.currentList[position]

        with(item) {
            val slNo = position + 1
            holder.binding.txtViewSlNo.text = slNo.toString()
            holder.binding.txtViewSenderAc.text = this.senderAccNumber
            holder.binding.txtViewReceiverName.text = this.receiverName
            holder.binding.txtViewReceiverAc.text = this.receiverAccNumber
            holder.binding.txtViewReceiverBank.text = this.receiverBank
            holder.binding.txtViewRouting.text = this.receiverRouting
            holder.binding.txtViewAmount.text = this.amount.toString()

            try {
                holder.binding.txtViewEntryDate.text = SimpleDateFormat(
                    Constant.DATE_FORMAT,
                    Locale.US
                ).format(SimpleDateFormat(Constant.DATE_FORMAT_API, Locale.US).parse(this.entryDate))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        holder.binding.imgViewEdit.setOnClickListener {
            onItemClickListener?.let { it(item) }
        }
    }

    fun setOnItemClickListener(listener: (Row) -> Unit) {
        onItemClickListener = listener
    }

}













