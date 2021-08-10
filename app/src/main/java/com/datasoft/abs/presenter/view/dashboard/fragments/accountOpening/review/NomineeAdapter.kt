package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.datasoft.abs.data.dto.createAccount.review.Nominee
import com.datasoft.abs.databinding.NomineeReviewRowBinding
import javax.inject.Inject

class NomineeAdapter @Inject constructor() :
    RecyclerView.Adapter<NomineeAdapter.NomineeViewHolder>() {

    inner class NomineeViewHolder(val binding: NomineeReviewRowBinding) :
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

        val binding =
            NomineeReviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NomineeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NomineeViewHolder, position: Int) {
        val value = differ.currentList[position]

        holder.binding.txtViewNomineeName.text = value.name
        holder.binding.txtViewNomineeRelation.text = value.relationshipValue
        val sharePercent = value.shareOfPercentage.toString() + "%"
        holder.binding.txtViewNomineeShare.text = sharePercent

        value.nomineeMinorInfo?.let {
            holder.binding.txtViewNomineeGuardian.text = it.nameOfGuardian
        }
    }
}













