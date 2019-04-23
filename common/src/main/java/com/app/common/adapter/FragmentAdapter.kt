package com.app.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter(fm: FragmentManager, var mData: List<Fragment>, var mTitles: List<String>? = null) : FragmentPagerAdapter(fm) {
    fun setData(data: List<Fragment>) {
        this.mData = data
    }

    override fun getItem(position: Int): Fragment {
        return mData.get(position)
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles?.get(position) ?: ""
    }
}