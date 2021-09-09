package com.datasoft.abs.presenter.view.dashboard.fragments.accountList.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.ActiveFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.AwaitingFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.accountList.DraftFragment
import javax.inject.Inject

class AccountMainAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
//            0 -> AllFragment()
            0 -> DraftFragment()
            1 -> AwaitingFragment()
            2 -> ActiveFragment()
            else -> DraftFragment()
        }
    }
}
