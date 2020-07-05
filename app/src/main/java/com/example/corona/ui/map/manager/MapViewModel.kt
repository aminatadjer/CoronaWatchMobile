package com.example.corona.ui.map.manager

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.corona.ui.map.global.MapAnimationGlobal
import com.example.corona.ui.map.local.MapAnimationLocal
import com.google.android.gms.maps.GoogleMap

class MapViewModel : ViewModel() {



    fun setLocalMap(googleMap: GoogleMap,context: Context,activity: FragmentActivity){
        val mapAnimation= MapAnimationLocal(
            googleMap,
            context,
            activity
        )
        mapAnimation.createMarkerList()
        mapAnimation.SetMarkerOnClickListner()
        mapAnimation.SetRegionOnClickListner()
    }

    fun setGlobalMap(googleMap: GoogleMap,context: Context,activity: FragmentActivity){
        val mapAnimation= MapAnimationGlobal(
            googleMap,
            context,
            activity
        )
        //mapAnimation.createMarkerList()
        //mapAnimation.SetMarkerOnClickListner()
        mapAnimation.SetRegionOnClickListner()
    }

}
