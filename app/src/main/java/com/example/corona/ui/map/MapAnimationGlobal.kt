package com.example.corona.ui.map

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R
import com.example.corona.ui.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.collections.GroundOverlayManager
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.collections.PolylineManager
import com.google.maps.android.data.Feature
import com.google.maps.android.data.Layer
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import com.google.maps.android.data.kml.KmlLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapAnimationGlobal (
    var googleMap: GoogleMap,
val context: Context,

val activity: FragmentActivity) {

    //KML RESOURCE
    var markerManager : MarkerManager =  MarkerManager(googleMap)
    var groundOverlayManager : GroundOverlayManager =  GroundOverlayManager(googleMap)
    var polygonManager : PolygonManager = PolygonManager(googleMap)
    var polylineManager : PolylineManager = PolylineManager(googleMap)

    var layerGeoJson: GeoJsonLayer =  GeoJsonLayer(googleMap, R.raw.world,context,markerManager,polygonManager,polylineManager, groundOverlayManager)

    //First show

    var enterGeoJson=false

    //Marker list
    var markerList=ArrayList<Marker>()
    var hospitals =ArrayList<com.example.corona.ui.map.Hospital>()

    //Bottom sheet items
    var bottom_sheet_global: LinearLayout =activity.findViewById(R.id.bottom_sheet_global)

    var region_global: TextView =activity.findViewById(R.id.region_global)
    var state_global: TextView =activity.findViewById(R.id.state_global)
    var nb_cases_global: TextView =activity.findViewById(R.id.nb_cases_global)
    var nb_holder_global: TextView =activity.findViewById(R.id.nb_holder_global)
    var nb_doubtful_global: TextView =activity.findViewById(R.id.nb_doubtful_global)
    var nb_deaths_global: TextView =activity.findViewById(R.id.nb_deaths_global)
    var nb_recovred_global: TextView =activity.findViewById(R.id.nb_recovred_global)



    //create Markers and draw circle around it for each wilaya
    fun createMarkerList(){
        val radius=13000.0
        for (key in LatLang.latLangWorld.keys){
            when(LatLang.latLangWorld[key]!!.degre){
                0->DrawCircle(googleMap,
                    LatLang.latLangWorld[key]!!.lat, LatLang.latLangWorld[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl1),radius)

                1->DrawCircle(googleMap,
                    LatLang.latLangWorld[key]!!.lat, LatLang.latLangWorld[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl2),radius)

                2->DrawCircle(googleMap,
                    LatLang.latLangWorld[key]!!.lat, LatLang.latLangWorld[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl3),radius)
            }

            markerList.add(
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(LatLang.latLangWorld[key]!!.lat, LatLang.latLangWorld[key]!!.lang))
                        .flat(true)
                        .title(key)
                        //.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(latLangAlgeria[key]!!.lat, latLangAlgeria[key]!!.lang,googleMap)))
                        .alpha(0.0f)))





        }




    }

    fun DrawCircle(gMap: GoogleMap, lat:Double, lng:Double, color:Int, radius:Double)
    {
        var circleMaker: CircleOptions =  CircleOptions();
        var latlong =  LatLng(lat, lng); //Location
        circleMaker.center(latlong)
        circleMaker.radius(radius)
        circleMaker.strokeWidth(3.0f)
        circleMaker.strokeColor(color)
        circleMaker.fillColor(color)
        val camera= CameraUpdateFactory.newLatLngZoom(latlong, 15.0f)
        val circle = gMap.addCircle(circleMaker)
    }


    fun SetMarkerOnClickListner(){
        googleMap.setOnMapLoadedCallback {
            googleMap.setOnMarkerClickListener {
                bottom_sheet_global.visibility= View.VISIBLE
                getCentreByRegion(  LatLang.latLangWorld[it.title]!!.id)

                setInfoWindowGlobal(
                    LatLang.latLangWorld[it.title]!!.ArabicName,
                    LatLang.latLangWorld[it.title]!!.degre,
                    LatLang.latLangWorld[it.title]!!.confirme.toString(),
                    LatLang.latLangWorld[it.title]!!.critique.toString(),
                    LatLang.latLangWorld[it.title]!!.suspect.toString(),
                    LatLang.latLangWorld[it.title]!!.mort.toString(),
                    LatLang.latLangWorld[it.title]!!.guerie.toString(),
                    hospitals)

                true
            }
        }
    }

    fun getCentreByRegion(idregion:Int){
        val retrofit = Retrofit.Builder()
            .baseUrl(Util.getProperty("baseUrl", context!!))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getCentreByRegion(idregion).enqueue(object: Callback<List<Hospital>> {
            override fun onResponse(call: Call<List<Hospital>>, response: retrofit2.Response<List<Hospital>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Hospital>? = response.body()
                    if (listBody != null) {
                        hospitals.clear()
                        hospitals.addAll(listBody)
                    }

                }
            }
            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {

            }
        })
    }

    fun SetRegionOnClickListner(){
        val style: GeoJsonPolygonStyle = layerGeoJson.getDefaultPolygonStyle()
        //style.setFillColor(Color.MAGENTA)
        style.setStrokeColor(Color.argb(90,60,40,120))
        style.setStrokeWidth(4F)
        layerGeoJson.addLayerToMap()
        layerGeoJson.setOnFeatureClickListener(object: GeoJsonLayer.GeoJsonOnFeatureClickListener {
            override fun onFeatureClick(feature: Feature?) {

                val property=feature!!.id

                GeoJsonMapAnimation(property)
                bottom_sheet_global.visibility= View.VISIBLE

                getCentreByRegion(LatLang.latLangWorld[property]!!.id)

                //set info window by KEY of HASHMAP  "latLangAlgeria[KEY]!!"
                setInfoWindowGlobal(
                    LatLang.latLangWorld[property]!!.ArabicName,
                    LatLang.latLangWorld[property]!!.degre,
                    LatLang.latLangWorld[property]!!.confirme.toString(),
                    LatLang.latLangWorld[property]!!.critique.toString(),
                    LatLang.latLangWorld[property]!!.suspect.toString(),
                    LatLang.latLangWorld[property]!!.mort.toString(),
                    LatLang.latLangWorld[property]!!.guerie.toString(),
                    hospitals)
            }
        })


    }



    fun setInfoWindowGlobal(region_:String,
                            state_:Int,
                            nb_cases_:String,
                            nb_holder_:String,
                            nb_doubtful_:String,
                            nb_deaths_:String,
                            nb_recovred_:String,
                            hospitals:MutableList<Hospital>){
        val recycler_view_global: RecyclerView =activity.findViewById(R.id.recycler_view_global)
        var etat:String=""
        region_global.text=region_
        when(state_){
            0->{etat=context.getString(R.string.danger_lvl1)
                state_global.setTextColor(Color.parseColor(Util.getProperty("dangerLvl1Color", context!!)))
            }
            1->{etat=context.getString(R.string.danger_lvl2)
                state_global.setTextColor(Color.parseColor(Util.getProperty("dangerLvl2Color", context!!)))
            }
            2->{etat=context.getString(R.string.danger_lvl3)
                state_global.setTextColor(Color.parseColor(Util.getProperty("dangerLvl3Color", context!!)));
            }
        }
        state_global.text=etat
        nb_cases_global.text=nb_cases_
        nb_holder_global.text=nb_holder_
        nb_doubtful_global.text=nb_doubtful_
        nb_deaths_global.text=nb_deaths_
        nb_recovred_global.text=nb_recovred_



        val recyclerView: RecyclerView = recycler_view_global
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)



        val adapter= HospitalAdapter()
        recyclerView.adapter=adapter
        adapter.setHospital(hospitals)
    }



    fun GeoJsonMapAnimation(Property:String){
        //markerList[latLangAlgeria.keys.indexOf(Property)].showInfoWindow()
        if(enterGeoJson){ layerGeoJson.removeLayerFromMap()
        }else{ enterGeoJson=true}

        layerGeoJson= GeoJsonLayer(googleMap, LatLang.latLangWorld[Property]!!.kmlResource,context,markerManager,polygonManager,polylineManager, groundOverlayManager)
        val style: GeoJsonPolygonStyle = layerGeoJson.getDefaultPolygonStyle()
        style.setStrokeColor(Color.argb(130,60,40,120))
        style.setStrokeWidth(9F)
        layerGeoJson.addLayerToMap()

    }


}