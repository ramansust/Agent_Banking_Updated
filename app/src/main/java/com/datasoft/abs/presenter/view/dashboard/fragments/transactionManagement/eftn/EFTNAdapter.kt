package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.eftn

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.inject.Inject

class EFTNAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
            0 -> AwaitingApprovalFragment()
            1 -> DisburseFragment()
            2 -> RejectFragment()
            else -> AwaitingApprovalFragment()
        }
    }
}
