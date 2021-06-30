package com.datasoft.abs.presenter.view.dashboard.fragments.account.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.account.ActiveFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.account.AllFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.account.AwaitingFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.account.DraftFragment
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
