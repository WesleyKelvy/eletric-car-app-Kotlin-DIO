package com.example.eletriccarapp.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.eletriccarapp.R
import com.example.eletriccarapp.ui.adapter.TabsAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : FragmentActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupTabs()
    }

    private fun setupView() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.vp_view_pager)
    }


    private fun setupTabs() {
        val tabsAdapter = TabsAdapter(this)
        viewPager.adapter = tabsAdapter

        tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.let {
                    viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })

        viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
    }
}