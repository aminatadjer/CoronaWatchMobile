package com.example.corona.ui.map

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap

class MapViewModel : ViewModel() {



    fun setLocalMap(googleMap: GoogleMap,context: Context,activity: FragmentActivity){
        val mapAnimation=MapAnimation(googleMap,context,activity)
        mapAnimation.createMarkerList("local")
        mapAnimation.SetMarkerOnClickListner("local")
        mapAnimation.SetRegionOnClickListner( "local")
    }

    fun setGlobalMap(googleMap: GoogleMap,context: Context,activity: FragmentActivity){
        val mapAnimation=MapAnimation(googleMap,context,activity)
        mapAnimation.createMarkerList("global")
        mapAnimation.SetMarkerOnClickListner("global")
        mapAnimation.SetRegionOnClickListner( "global")
    }

}
