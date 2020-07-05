package com.example.corona.ui.map.local

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
import com.example.corona.ui.map.LatLang
import com.example.corona.ui.map.Service
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
import com.google.maps.android.data.kml.KmlLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapAnimationLocal(
    var googleMap: GoogleMap,
    val context:Context,

    val activity: FragmentActivity) {

    //KML RESOURCE
     var markerManager : MarkerManager=  MarkerManager(googleMap)
     var groundOverlayManager : GroundOverlayManager=  GroundOverlayManager(googleMap)
     var polygonManager : PolygonManager=PolygonManager(googleMap)
     var polylineManager : PolylineManager=PolylineManager(googleMap)
     var kmlPolylineLayer : KmlLayer=KmlLayer(googleMap,R.raw.dza1, context, markerManager, polygonManager, polylineManager, groundOverlayManager, null)

    //First show
    var enterKml:Boolean=false


    //Marker list
    var markerList=ArrayList<Marker>()
    var hospitals =ArrayList<Hospital>()

    //Bottom sheet items
    var bottom_sheet_local:LinearLayout=activity.findViewById(R.id.bottom_sheet_local)

    var region_local:TextView=activity.findViewById(R.id.region_local)
    var state_local:TextView=activity.findViewById(R.id.state_local)
    var nb_cases_local:TextView=activity.findViewById(R.id.nb_cases_local)
    var nb_holder_local:TextView=activity.findViewById(R.id.nb_holder_local)
    var nb_doubtful_local:TextView=activity.findViewById(R.id.nb_doubtful_local)
    var nb_deaths_local:TextView=activity.findViewById(R.id.nb_deaths_local)
    var nb_recovred_local:TextView=activity.findViewById(R.id.nb_recovred_local)

    //create Markers and draw circle around it for each wilaya
    fun createMarkerList(){
        val radius=13000.0
        for (key in LatLang.latLangAlgeria.keys){
            when(LatLang.latLangAlgeria[key]!!.degre){
                0->DrawCircle(googleMap,
                    LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl1),radius)

                1->DrawCircle(googleMap,
                    LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl2),radius)

                2->DrawCircle(googleMap,
                    LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang,
                    ContextCompat.getColor(context, R.color.danger_lvl3),radius)
            }
            markerList.add(
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(LatLang.latLangAlgeria[key]!!.lat, LatLang.latLangAlgeria[key]!!.lang))
                        .flat(true)
                        .title(key)
                        //.icon(BitmapDescriptorFactory.fromBitmap(getBitmap(latLangAlgeria[key]!!.lat, latLangAlgeria[key]!!.lang,googleMap)))
                        .alpha(0.0f)))


        }
        kmlPolylineLayer.groundOverlays
        kmlPolylineLayer.addLayerToMap()

    }

    fun DrawCircle( gMap:GoogleMap,lat:Double,lng:Double,color:Int,radius:Double)
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
                kmlMapAnimation(it.title)

                bottom_sheet_local.visibility= View.VISIBLE
                getCentreByRegion(  LatLang.latLangAlgeria[it.title]!!.id)

                setInfoWindowLocal(
                    LatLang.latLangAlgeria[it.title]!!.ArabicName,
                    LatLang.latLangAlgeria[it.title]!!.degre,
                    LatLang.latLangAlgeria[it.title]!!.confirme.toString(),
                    LatLang.latLangAlgeria[it.title]!!.critique.toString(),
                    LatLang.latLangAlgeria[it.title]!!.suspect.toString(),
                    LatLang.latLangAlgeria[it.title]!!.mort.toString(),
                    LatLang.latLangAlgeria[it.title]!!.guerie.toString(),
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
        val service = retrofit.create<Service>(
            Service::class.java)
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


                kmlPolylineLayer.setOnFeatureClickListener(object : Layer.OnFeatureClickListener{
                    override fun onFeatureClick(feature: Feature?) {

                        val property="name"
                        kmlMapAnimation(feature!!.getProperty(property))
                        bottom_sheet_local.visibility=View.VISIBLE

                        getCentreByRegion(LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.id)

                        //set info window by KEY of HASHMAP  "latLangAlgeria[KEY]!!"
                        setInfoWindowLocal(
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.ArabicName,
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.degre,
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.confirme.toString(),
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.critique.toString(),
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.suspect.toString(),
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.mort.toString(),
                            LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.guerie.toString(),
                            hospitals)

                    } })




    }

    fun setInfoWindowLocal(region_:String,
                      state_:Int,
                      nb_cases_:String,
                      nb_holder_:String,
                      nb_doubtful_:String,
                      nb_deaths_:String,
                      nb_recovred_:String,
                      hospitals:MutableList<Hospital>){
        val recycler_view_local:RecyclerView=activity.findViewById(R.id.recycler_view_local)
        var etat:String=""
        region_local.text=region_
        when(state_){
            0->{etat=context.getString(R.string.danger_lvl1)
                state_local.setTextColor(Color.parseColor(Util.getProperty("dangerLvl1Color", context!!)))
            }
            1->{etat=context.getString(R.string.danger_lvl2)
                state_local.setTextColor(Color.parseColor(Util.getProperty("dangerLvl2Color", context!!)))
            }
            2->{etat=context.getString(R.string.danger_lvl3)
                state_local.setTextColor(Color.parseColor(Util.getProperty("dangerLvl3Color", context!!)));
            }
        }
        state_local.text=etat
        nb_cases_local.text=nb_cases_
        nb_holder_local.text=nb_holder_
        nb_doubtful_local.text=nb_doubtful_
        nb_deaths_local.text=nb_deaths_
        nb_recovred_local.text=nb_recovred_



        val recyclerView: RecyclerView = recycler_view_local
        recyclerView.layoutManager= LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)



        val adapter= HospitalAdapter()
        recyclerView.adapter=adapter
        adapter.setHospital(hospitals)
    }





    fun kmlMapAnimation(Property:String){
        //markerList[latLangAlgeria.keys.indexOf(Property)].showInfoWindow()
        if(enterKml){ kmlPolylineLayer.removeLayerFromMap()
        }else{ enterKml=true}
        kmlPolylineLayer=KmlLayer(googleMap,
            LatLang.latLangAlgeria[Property]!!.kmlResource, context, markerManager, polygonManager, polylineManager, groundOverlayManager, null)
        kmlPolylineLayer.addLayerToMap()

    }



}