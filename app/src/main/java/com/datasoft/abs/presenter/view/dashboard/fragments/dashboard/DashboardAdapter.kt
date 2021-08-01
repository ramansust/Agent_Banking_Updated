package com.datasoft.abs.presenter.view.dashboard.fragments.dashboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DashboardAdapter constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
            0 -> TransactionFragment()
            1 -> LoanFragment()
            else -> TransactionFragment()
        }
    }
}
