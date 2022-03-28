package com.erdiansyah.githubusers2.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(activity: AppCompatActivity, bundle: Bundle) : FragmentStateAdapter(activity) {
    private var dataBundle: Bundle = bundle

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = UseListFragment()
        when (position){
            0 -> dataBundle.putString(UseListFragment.TAB_NAME, UseListFragment.TAB_FOLLOWERS)
            1 -> dataBundle.putString(UseListFragment.TAB_NAME, UseListFragment.TAB_FOLLOWING)
        }
        fragment.arguments = this.dataBundle
        return fragment
    }
}