package com.android.battery.saver.UI

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.battery.saver.R
import com.android.battery.saver.UI.tabs.MainFragment
import com.android.battery.saver.UI.tabs.SettingsFragment
import com.android.battery.saver.UI.tabs.UsageStatsFragment

class PageHolderAdapter(context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mContext = context
    private val tabsArrayList = arrayOf(
        R.string.mainFragment,
        R.string.usageStats,
        R.string.settings
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> UsageStatsFragment()
            else -> SettingsFragment()
        }
    }

    override fun getCount(): Int {
        return tabsArrayList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.getString(tabsArrayList[position])
    }


}