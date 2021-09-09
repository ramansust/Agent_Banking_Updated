package com.datasoft.abs.presenter.view.dashboard.fragments.customerList.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.ActiveFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.AwaitingFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.DraftFragment
import javax.inject.Inject

class CustomerListMainAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
            0 -> DraftFragment()
            1 -> AwaitingFragment()
            2 -> ActiveFragment()
            else -> DraftFragment()
        }
    }
}
