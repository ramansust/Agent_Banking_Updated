package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.createCustomer.DocumentInfo
import com.datasoft.abs.databinding.DocumentAttachRowBinding
import javax.inject.Inject

class DocumentListAdapter @Inject constructor() :
    RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(val binding: DocumentAttachRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<DocumentInfo>() {
        override fun areItemsTheSame(oldItem: DocumentInfo, newItem: DocumentInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DocumentInfo, newItem: DocumentInfo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {

        val binding = DocumentAttachRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((DocumentInfo) -> Unit)? = null

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewDocumentName.text = value.name
        holder.binding.txtViewTracingId.text = value.tracingID
        holder.binding.txtViewIssueDate.text = value.issueDate
        holder.binding.txtViewExpiryDate.text = value.expiryDate

        holder.binding.imgViewPhoto.setImageURI(Uri.parse(value.frontUri))

        holder.binding.imgViewDelete.setOnClickListener {
            onItemClickListener?.let { it(value) }
        }
    }

    fun setOnItemClickListener(listener: (DocumentInfo) -> Unit) {
        onItemClickListener = listener
    }
}













