package com.datasoft.abs.presenter.view.dashboard.fragments.customerCreate.kyc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.createCustomer.AddressInfo
import com.datasoft.abs.databinding.DocumentRowBinding
import javax.inject.Inject

class VerifyListAdapter @Inject constructor() :
    RecyclerView.Adapter<VerifyListAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(val binding: DocumentRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CommonModel>() {
        override fun areItemsTheSame(oldItem: CommonModel, newItem: CommonModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommonModel, newItem: CommonModel): Boolean {
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

    private var onItemClickListener: ((AddressInfo) -> Unit)? = null

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val value = differ.currentList[position]

        with(value) {
            holder.binding.txtViewDocumentName.text = value.name
        }

//        holder.binding.imgViewDelete.setOnClickListener {
//            onItemClickListener?.let { it(value) }
//        }
    }

    fun setOnItemClickListener(listener: (AddressInfo) -> Unit) {
        onItemClickListener = listener
    }
}













