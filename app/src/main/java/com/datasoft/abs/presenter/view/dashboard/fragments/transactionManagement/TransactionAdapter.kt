package com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.balanceInquiry.BalanceInquiryFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.deposit.DepositFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.transfer.TransferFragment
import com.datasoft.abs.presenter.view.dashboard.fragments.transactionManagement.transaction.withdraw.WithdrawFragment
import javax.inject.Inject

class TransactionAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        return when (position) {
            0 -> DepositFragment()
            1 -> WithdrawFragment()
            2 -> TransferFragment()
            3 -> BalanceInquiryFragment()
            else -> DepositFragment()
        }
    }
}
