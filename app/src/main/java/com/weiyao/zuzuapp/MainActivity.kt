package com.weiyao.zuzuapp

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.common.adapter.FragmentAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.weiyao.zuzuapp.base.BaseActivity
import com.weiyao.zuzuapp.fragment.ApiFragment
import com.weiyao.zuzuapp.fragment.MyViewFragment
import com.weiyao.zuzuapp.fragment.OtherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun bindLayout(): Int = R.layout.activity_main

    override fun initView() {
        super.initView()
        val fragmentList = listOf<Fragment>(MyViewFragment(), ApiFragment(), OtherFragment());
        vpMain.adapter = FragmentAdapter(supportFragmentManager, fragmentList)
    }

    override fun initListener() {
        super.initListener()
        navMain.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        vpMain.addOnPageChangeListener(onPageChangeListener)
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            navMain.menu.getItem(position).isChecked = true
        }

        override fun onPageSelected(position: Int) {
        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                vpMain.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                vpMain.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                vpMain.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
