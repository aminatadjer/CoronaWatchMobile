package com.example.corona.ui.video

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private val fragments=ArrayList<Fragment>()
    private val fragmentsTitle=ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    fun addFragment(fragment:Fragment,title:String){
        fragments.add(fragment)
        fragmentsTitle.add(title)

    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentsTitle.get(position  )
    }
}