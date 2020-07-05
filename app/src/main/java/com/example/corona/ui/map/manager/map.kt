package com.example.corona.ui.map.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import com.example.corona.R
import com.example.corona.ui.map.global.GlobalMap
import com.example.corona.ui.map.local.LocalMap

import com.google.android.material.tabs.TabLayout
import me.ibrahimsn.lib.SmoothBottomBar


@Suppress("CAST_NEVER_SUCCEEDS")
class map : Fragment(){

    companion object {
        fun newInstance() = map()
    }

    lateinit var toolbar: SmoothBottomBar

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private lateinit var fragment_global_map: GlobalMap
    private lateinit var fragment_local_map: LocalMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar = activity!!.findViewById(R.id.bottom_bar)
        toolbar.visibility=View.VISIBLE

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= "الخريطة"
        tolb.visibility=View.GONE

        viewPager=activity!!.findViewById(R.id.view_pager_map)
        tabLayout=activity!!.findViewById(R.id.tab_layout_map)



        fragment_global_map= GlobalMap()
        fragment_local_map= LocalMap()

        tabLayout.setupWithViewPager(viewPager)

        val viewPagerAdapter=
            ViewPagerAdapter(
                childFragmentManager,
                0
            )
        viewPagerAdapter.addFragment(fragment_local_map,"الخريطة المحلية")
        viewPagerAdapter.addFragment(fragment_global_map,"الخريطة العالمية")

        viewPager.adapter=viewPagerAdapter



    }



}
