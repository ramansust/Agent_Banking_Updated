package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.transaction.TransactionDetailsResponseItem
import com.datasoft.abs.databinding.TransactionDetailsRowBinding
import com.datasoft.abs.presenter.utils.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TransactionDetailsAdapter @Inject constructor() :
    RecyclerView.Adapter<TransactionDetailsAdapter.TransactionDetailsViewHolder>() {

    inner class TransactionDetailsViewHolder(val binding: TransactionDetailsRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TransactionDetailsResponseItem>() {
        override fun areItemsTheSame(
            oldItem: TransactionDetailsResponseItem,
            newItem: TransactionDetailsResponseItem
        ): Boolean {
            return oldItem.transactionNo == newItem.transactionNo
        }

        override fun areContentsTheSame(
            oldItem: TransactionDetailsResponseItem,
            newItem: TransactionDetailsResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailsViewHolder {

        val binding =
            TransactionDetailsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TransactionDetailsViewHolder, position: Int) {
        val account = differ.currentList[position]

        with(account) {
            holder.binding.txtViewTransactionNo.text = this.transactionNo
            holder.binding.txtViewTransactionType.text = this.transactionType

            try {
                holder.binding.txtViewDateTime.text = SimpleDateFormat(
                    Constant.DATE_FORMAT,
                    Locale.US
                ).format(SimpleDateFormat(Constant.DATE_FORMAT_API, Locale.US).parse(this.transactionDate))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            holder.binding.txtViewDescription.text = this.narration
            holder.binding.txtViewDebit.text = this.debit.toString()
            holder.binding.txtViewCredit.text = this.credit.toString()
        }
    }

}













