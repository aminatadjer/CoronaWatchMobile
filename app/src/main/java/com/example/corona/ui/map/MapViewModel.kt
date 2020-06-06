package com.example.corona.ui.map

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap

class MapViewModel : ViewModel() {



    fun setMap(googleMap: GoogleMap,context: Context,activity: FragmentActivity){
        val mapAnimation=MapAnimation(googleMap,context,activity)
        mapAnimation.createMarkerList()
        mapAnimation.SetMarkerOnClickListner()
        mapAnimation.SetRegionOnClickListner()
    }

}
