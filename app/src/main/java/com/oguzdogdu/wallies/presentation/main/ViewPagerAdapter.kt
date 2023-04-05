package com.oguzdogdu.wallies.presentation.main

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oguzdogdu.wallies.presentation.latest.LatestFragment
import com.oguzdogdu.wallies.presentation.popular.PopularFragment

class ViewPagerAdapter(container: FragmentActivity, private val fragmentList: List<Fragment>) :
    FragmentStateAdapter(container) {
    override fun getItemCount() = fragmentList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun createFragment(position: Int) : Fragment {
        return when(position){
            0 -> {PopularFragment()}
            1 -> {LatestFragment()}
            else -> {throw Resources.NotFoundException("Position Not Found")}
        }
    }
}