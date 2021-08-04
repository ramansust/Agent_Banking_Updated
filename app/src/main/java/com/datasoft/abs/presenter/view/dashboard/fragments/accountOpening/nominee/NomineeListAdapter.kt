package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.nominee

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.databinding.AddressRowBinding
import javax.inject.Inject

class NomineeListAdapter @Inject constructor() :
    RecyclerView.Adapter<NomineeListAdapter.NomineeViewHolder>() {

    inner class NomineeViewHolder(val binding: AddressRowBinding) :
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

        val binding = AddressRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NomineeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Nominee) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NomineeViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewAddress.text = "${position + 1}. ${value.name}, ${value.motherName}, ${value.fatherName}, ${value.dob}, ${value.shareOfPercentage}"

        holder.binding.imgViewDelete.setOnClickListener {
            onItemClickListener?.let { it(value) }
        }
    }

    fun setOnItemClickListener(listener: (Nominee) -> Unit) {
        onItemClickListener = listener
    }
}













