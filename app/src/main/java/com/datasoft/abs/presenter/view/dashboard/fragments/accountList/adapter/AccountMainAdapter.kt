package com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.ActiveFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.AllFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.AwaitingFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.DraftFragment
import javax.inject.Inject

class AccountMainAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

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
