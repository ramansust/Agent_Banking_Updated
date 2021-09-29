package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.createCustomer.DocumentVerificationInfo
import com.datasoft.abs.databinding.DocumentRowBinding
import javax.inject.Inject

class VerifyListAdapter @Inject constructor() :
    RecyclerView.Adapter<VerifyListAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(val binding: DocumentRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<DocumentVerificationInfo>() {
        override fun areItemsTheSame(
            oldItem: DocumentVerificationInfo,
            newItem: DocumentVerificationInfo
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DocumentVerificationInfo,
            newItem: DocumentVerificationInfo
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {

        val binding = DocumentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onPhotocopyClickListener: ((Int, Boolean) -> Unit)? = null
    private var onVerifiedClickListener: ((Int, Boolean) -> Unit)? = null

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val value = differ.currentList[position]

        with(value) {
            holder.binding.txtViewDocumentName.text = this.name
            holder.binding.switchCollected.isChecked = this.isPhotocopyCollected
            holder.binding.switchVerified.isChecked = this.isVerified
        }

        holder.binding.switchCollected.setOnCheckedChangeListener { _, isChecked ->
            onPhotocopyClickListener?.let { it(value.id, isChecked) }
        }

        holder.binding.switchVerified.setOnCheckedChangeListener { _, isChecked ->
            onVerifiedClickListener?.let { it(value.id, isChecked) }
        }
    }

    fun setOnPhotocopyClickListener(listener: (Int, Boolean) -> Unit) {
        onPhotocopyClickListener = listener
    }

    fun setOnVerifiedClickListener(listener: (Int, Boolean) -> Unit) {
        onVerifiedClickListener = listener
    }
}













