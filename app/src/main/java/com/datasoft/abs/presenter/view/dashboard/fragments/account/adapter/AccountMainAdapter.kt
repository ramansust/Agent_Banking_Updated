package com.datasoft.abs.presenter.view.dashboard.fragments.account.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.account.AccountFragment
import javax.inject.Inject

class AccountMainAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return AccountFragment()
    }
}
