package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.others

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.source.local.db.entity.account.OtherFacilities
import com.datasoft.abs.databinding.OtherFacilityRowBinding
import javax.inject.Inject

class OtherFacilityAdapter @Inject constructor() :
    RecyclerView.Adapter<OtherFacilityAdapter.OtherFacilityViewHolder>() {

    inner class OtherFacilityViewHolder(val binding: OtherFacilityRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<OtherFacilities>() {
        override fun areItemsTheSame(oldItem: OtherFacilities, newItem: OtherFacilities): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: OtherFacilities,
            newItem: OtherFacilities
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherFacilityViewHolder {

        val binding =
            OtherFacilityRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtherFacilityViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int, Boolean) -> Unit)? = null

    override fun onBindViewHolder(holder: OtherFacilityViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.title.text = value.name
        holder.binding.switchView.isChecked = value.isChecked!!

        holder.binding.switchView.setOnCheckedChangeListener { _, isChecked ->
            onItemClickListener?.let { it(value.id, isChecked) }
        }
    }

    fun setOnItemClickListener(listener: (Int, Boolean) -> Unit) {
        onItemClickListener = listener
    }
}













