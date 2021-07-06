package com.datasoft.abs.presenter.view.dashboard.fragments.customerList.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.ActiveFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.AllFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.AwaitingFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.customerList.DraftFragment
import javax.inject.Inject

class CustomerListMainAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
            0 -> AllFragment()
            1 -> ActiveFragment()
            2 -> AwaitingFragment()
            3 -> DraftFragment()
            else -> AllFragment()
        }
    }
}
