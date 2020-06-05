package com.example.corona.ui.map

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.corona.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.kml.KmlLayer
import kotlinx.android.synthetic.main.bottom_sheet.*

class MapAnimation(var googleMap: GoogleMap,val context:Context,val activity: FragmentActivity) {
    //KML RESOURCE
     var markerManager : MarkerManager=  MarkerManager(googleMap)
     var groundOverlayManager : GroundOverlayManager=  GroundOverlayManager(googleMap)
     var polygonManager : PolygonManager=PolygonManager(googleMap)
     var polylineManager : PolylineManager=PolylineManager(googleMap)
     var kmlPolylineLayer : KmlLayer=KmlLayer(googleMap,R.raw.dza1, context, markerManager, polygonManager, polylineManager, groundOverlayManager, null)

    //First show
    var enter:Boolean=false

    //Marker list
    var markerList=ArrayList<Marker>()

    //Bottom sheet items
    var bottom_sheet:LinearLayout=activity.findViewById(R.id.bottom_sheet)
    var region:TextView=activity.findViewById(R.id.region)
    var state:TextView=activity.findViewById(R.id.state)
    var nb_cases:TextView=activity.findViewById(R.id.nb_cases)
    var nb_holder:TextView=activity.findViewById(R.id.nb_holder)
    var nb_doubtful:TextView=activity.findViewById(R.id.nb_doubtful)
    var nb_deaths:TextView=activity.findViewById(R.id.nb_deaths)
    var nb_recovred:TextView=activity.findViewById(R.id.nb_recovred)



    fun createMarkerList(){
        for (key in LatLang.latLangAlgeria.keys){
            DrawCircle(googleMap,
                LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang,"#80FF0000",30000.0)
            markerList.add(
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang))
                        .flat(true)
                        .title(key)
                        //.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(latLangAlgeria[key]!!.lat, latLangAlgeria[key]!!.lang,googleMap)))
                        .alpha(0.0f)))
        }
        kmlPolylineLayer.addLayerToMap()
    }

    fun DrawCircle( gMap:GoogleMap,lat:Double,lng:Double,color:String,radius:Double)
    {

        var circleMaker: CircleOptions =  CircleOptions();
        var latlong =  LatLng(lat, lng); //Location
        circleMaker.center(latlong)
        circleMaker.radius(radius)
        circleMaker.strokeWidth(3.0f)
        circleMaker.strokeColor(Color.parseColor(color))
        circleMaker.fillColor(Color.parseColor(color))
        val camera= CameraUpdateFactory.newLatLngZoom(latlong, 15.0f)

        val circle = gMap.addCircle(circleMaker)
    }

    fun SetMarkerOnClickListner(){
        googleMap.setOnMapLoadedCallback {
            googleMap.setOnMarkerClickListener {
                mapAnimation(it.title)
                bottom_sheet.visibility= View.VISIBLE
                setInfoWindow(
                    LatLang.latLangAlgeria[it.title]!!.ArabicName,
                    "danger",
                    "100",
                    "101",
                    "102",
                    "103",
                    "104")
                true
            }
        }
    }

    fun SetRegionOnClickListner(){
        kmlPolylineLayer.setOnFeatureClickListener(object : Layer.OnFeatureClickListener{
            override fun onFeatureClick(feature: Feature?) {
                mapAnimation(feature!!.getProperty("name"))
                bottom_sheet.visibility=View.VISIBLE

                //set info window by KEY of HASHMAP  "latLangAlgeria[KEY]!!"
                setInfoWindow(
                    LatLang.latLangAlgeria[feature!!.getProperty("name")]!!.ArabicName,
                    "خطير",
                    "100",
                    "101",
                    "102",
                    "103",
                    "104")

            } })
    }

    fun setInfoWindow(region_:String,
                      state_:String,
                      nb_cases_:String,
                      nb_holder_:String,
                      nb_doubtful_:String,
                      nb_deaths_:String,
                      nb_recovred_:String){
        region.text=region_
        state.text=state_
        nb_cases.text=nb_cases_
        nb_holder.text=nb_holder_
        nb_doubtful.text=nb_doubtful_
        nb_deaths.text=nb_deaths_
        nb_recovred.text=nb_recovred_

    }

    fun mapAnimation(Property:String){
        //markerList[latLangAlgeria.keys.indexOf(Property)].showInfoWindow()
        if(enter){ kmlPolylineLayer.removeLayerFromMap()
        }else{ enter=true}
        kmlPolylineLayer=KmlLayer(googleMap,
            LatLang.latLangAlgeria[Property]!!.kmlResource, context, markerManager, polygonManager, polylineManager, groundOverlayManager, null)
        kmlPolylineLayer.addLayerToMap()

    }


}