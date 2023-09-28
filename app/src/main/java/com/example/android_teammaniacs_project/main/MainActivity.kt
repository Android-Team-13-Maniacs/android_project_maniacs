package com.example.android_teammaniacs_project.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.databinding.MainActivityBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        //viewpager adapter
        viewPager.adapter = viewPagerAdapter
        //viewpager slide
        viewPager.isUserInputEnabled=false


        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        //Tab icon 설정
        //Tab 아이콘 설정
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_search)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_myvideos)
    }
}