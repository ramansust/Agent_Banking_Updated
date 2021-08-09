package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.databinding.NomineeRowBinding
import javax.inject.Inject

class NomineeListAdapter @Inject constructor(val glide: RequestManager) :
    RecyclerView.Adapter<NomineeListAdapter.NomineeViewHolder>() {

    inner class NomineeViewHolder(val binding: NomineeRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Nominee>() {
        override fun areItemsTheSame(oldItem: Nominee, newItem: Nominee): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Nominee, newItem: Nominee): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NomineeViewHolder {

        val binding = NomineeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NomineeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Nominee) -> Unit)? = null

    override fun onBindViewHolder(holder: NomineeViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewNameValue.text = value.name
        holder.binding.txtViewRelationValue.text = value.relationshipValue
        val sharePercent = value.shareOfPercentage.toString() + "%"
        holder.binding.txtViewPercentShareValue.text = sharePercent

        if (value.photo.isNotEmpty() && Uri.parse(value.photo).isAbsolute) {
            holder.binding.imgViewNomineePhoto.setImageURI(Uri.parse(value.photo))
        } else if (value.photo.isNotEmpty()) {
            glide.load(Base64.decode(value.photo, Base64.DEFAULT))
                .into(holder.binding.imgViewNomineePhoto)
        }

        holder.binding.imgViewDelete.setOnClickListener {
            onItemClickListener?.let { it(value) }
        }
    }

    fun setOnItemClickListener(listener: (Nominee) -> Unit) {
        onItemClickListener = listener
    }
}













