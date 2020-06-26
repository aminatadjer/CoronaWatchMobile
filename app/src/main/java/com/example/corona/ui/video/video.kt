package com.example.corona.ui.video

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager

import com.example.corona.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class video : Fragment() {

    companion object {
        fun newInstance() = video()
    }

    private lateinit var viewModel: VideoViewModel

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private lateinit var fragmentSpiderVideo: SpiderVideo
    private lateinit var fragmentUserVideo: UserVideo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        // TODO: Use the ViewModel
        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= "فيديوهات"
        tolb.visibility=View.GONE

        viewPager=activity!!.findViewById(R.id.view_pager)
        tabLayout=activity!!.findViewById(R.id.tab_layout)

        fragmentSpiderVideo= SpiderVideo()
        fragmentUserVideo= UserVideo()

        tabLayout.setupWithViewPager(viewPager)

        val viewPagerAdapter=ViewPagerAdapter(childFragmentManager,0)
        viewPagerAdapter.addFragment(fragmentSpiderVideo,"مستجدات")
        viewPagerAdapter.addFragment(fragmentUserVideo,"User")
        viewPager.adapter=viewPagerAdapter



    }


}
