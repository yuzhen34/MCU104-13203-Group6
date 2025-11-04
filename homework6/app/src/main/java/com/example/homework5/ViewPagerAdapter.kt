package com.example.homework5

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // 一共有 3 個頁面：Start、Progress、Result
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StartFragment()     // 第一頁
            1 -> ProgressFragment()  // 第二頁
            2 -> ResultFragment()    // 第三頁
            else -> StartFragment()
        }
    }
}