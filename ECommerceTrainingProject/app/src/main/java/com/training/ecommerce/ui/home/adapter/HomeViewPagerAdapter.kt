package com.training.ecommerce.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(
    fragmentActivity: FragmentActivity, val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}