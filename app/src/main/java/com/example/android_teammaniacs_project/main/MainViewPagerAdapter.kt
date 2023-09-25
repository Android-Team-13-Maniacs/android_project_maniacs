package com.example.android_teammaniacs_project.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.Test1Fragment
import com.example.android_teammaniacs_project.Test2Fragment
import com.example.android_teammaniacs_project.Test3Fragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<MainTabs>()

//    페이지 생성 관리
    init {
        fragments.add(
            MainTabs(Test1Fragment.newInstance(), R.string.main_tab_home_title)
        )
        fragments.add(
            MainTabs(Test2Fragment.newInstance(), R.string.main_tab_search_title),
        )
        fragments.add(
            MainTabs(Test3Fragment.newInstance(), R.string.main_tab_my_videos_title),
        )
    }

    fun getTitle(position: Int): Int {
        return fragments[position].titleRes
    }

    //화면의 갯수
    override fun getItemCount(): Int {
        return fragments.size
    }
    
    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
}