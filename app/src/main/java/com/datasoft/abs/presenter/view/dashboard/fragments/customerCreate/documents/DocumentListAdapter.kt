package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.documents

import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.datasoft.abs.data.dto.createCustomer.RelatedDoc
import com.datasoft.abs.databinding.DocumentAttachRowBinding
import javax.inject.Inject

class DocumentListAdapter @Inject constructor(val glide: RequestManager) :
    RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(val binding: DocumentAttachRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RelatedDoc>() {
        override fun areItemsTheSame(oldItem: RelatedDoc, newItem: RelatedDoc): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RelatedDoc, newItem: RelatedDoc): Boolean {
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

    private var onItemClickListener: ((RelatedDoc) -> Unit)? = null

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewDocumentName.text = value.docTypeName
        holder.binding.txtViewTracingId.text = value.tracingId
        holder.binding.txtViewIssueDate.text = value.issueDate
        holder.binding.txtViewExpiryDate.text = value.expiredDate

//        glide.load(Base64.decode(value.frontSideImage, Base64.DEFAULT)).into(holder.binding.imgViewPhoto)
        holder.binding.imgViewPhoto.setImageURI(Uri.parse(value.frontSideImage))

        holder.binding.imgViewDelete.setOnClickListener {
            onItemClickListener?.let { it(value) }
        }
    }

    fun setOnItemClickListener(listener: (RelatedDoc) -> Unit) {
        onItemClickListener = listener
    }
}













