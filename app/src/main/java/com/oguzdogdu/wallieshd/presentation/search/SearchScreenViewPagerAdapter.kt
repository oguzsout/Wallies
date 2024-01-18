package com.oguzdogdu.wallieshd.presentation.search

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.oguzdogdu.wallieshd.presentation.profiledetail.usercollections.UserCollectionsFragment
import com.oguzdogdu.wallieshd.presentation.search.searchphoto.SearchPhotoFragment

class SearchScreenViewPagerAdapter(
    container: FragmentActivity,
    private val fragmentList: List<Fragment>
) :
    FragmentStateAdapter(container) {
    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                SearchPhotoFragment()
            }

            1 -> {
                UserCollectionsFragment()
            }

            else -> {
                throw Resources.NotFoundException("Position Not Found")
            }
        }
    }
}
