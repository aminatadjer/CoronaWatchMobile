package com.example.corona.ui.map

import com.example.corona.ui.Util
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

import com.example.corona.R
import com.example.corona.ui.map.LatLang.latLangAlgeria

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.kml.KmlContainer
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlLineString
import com.google.maps.android.data.kml.KmlPoint
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.map_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class map : Fragment(){

    companion object {
        fun newInstance() = map()
    }


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

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= "الخريطة"
        tolb.visibility=View.GONE

        viewPager=activity!!.findViewById(R.id.view_pager_map)
        tabLayout=activity!!.findViewById(R.id.tab_layout_map)



        fragment_global_map= GlobalMap()
        fragment_local_map= LocalMap()

        tabLayout.setupWithViewPager(viewPager)

        val viewPagerAdapter=ViewPagerAdapter(childFragmentManager,0)
        viewPagerAdapter.addFragment(fragment_local_map,"local")
        viewPagerAdapter.addFragment(fragment_global_map,"global")

        viewPager.adapter=viewPagerAdapter



    }

}
