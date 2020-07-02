package com.example.corona.ui.map

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

class MapAnimation(
    var googleMap: GoogleMap,
    val context:Context,

    val activity: FragmentActivity) {

    //KML RESOURCE
     var markerManager : MarkerManager=  MarkerManager(googleMap)
     var groundOverlayManager : GroundOverlayManager=  GroundOverlayManager(googleMap)
     var polygonManager : PolygonManager=PolygonManager(googleMap)
     var polylineManager : PolylineManager=PolylineManager(googleMap)
     var kmlPolylineLayer : KmlLayer=KmlLayer(googleMap,R.raw.dza1, context, markerManager, polygonManager, polylineManager, groundOverlayManager, null)

    var layerGeoJson: GeoJsonLayer =  GeoJsonLayer(googleMap, R.raw.world,context,markerManager,polygonManager,polylineManager, groundOverlayManager)

    //First show
    var enterKml:Boolean=false
    var enterGeoJson=false

    //Marker list
    var markerList=ArrayList<Marker>()
    var hospitals =ArrayList<com.example.corona.ui.map.Hospital>()

    //Bottom sheet items
    var bottom_sheet:LinearLayout=activity.findViewById(R.id.bottom_sheet)
    var region:TextView=activity.findViewById(R.id.region)
    var state:TextView=activity.findViewById(R.id.state)
    var nb_cases:TextView=activity.findViewById(R.id.nb_cases)
    var nb_holder:TextView=activity.findViewById(R.id.nb_holder)
    var nb_doubtful:TextView=activity.findViewById(R.id.nb_doubtful)
    var nb_deaths:TextView=activity.findViewById(R.id.nb_deaths)
    var nb_recovred:TextView=activity.findViewById(R.id.nb_recovred)


    //create Markers and draw circle around it for each wilaya
    fun createMarkerList(locGlo:String){
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

        when(locGlo){
            "local"->{
                kmlPolylineLayer.addLayerToMap()
            }
            "global"->{
                val style: GeoJsonPolygonStyle = layerGeoJson.getDefaultPolygonStyle()
                //style.setFillColor(Color.MAGENTA)
                style.setStrokeColor(Color.argb(90,60,40,120))
                style.setStrokeWidth(4F)

                layerGeoJson.addLayerToMap()
            }
        }


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
                bottom_sheet.visibility= View.VISIBLE

                getCentreByRegion(  LatLang.latLangAlgeria[it.title]!!.id)

                setInfoWindow(
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

    fun SetRegionOnClickListner(locGlo:String){

        when(locGlo){
            "local"->{
                kmlPolylineLayer.setOnFeatureClickListener(object : Layer.OnFeatureClickListener{
                    override fun onFeatureClick(feature: Feature?) {

                        val property="name"
                        kmlMapAnimation(feature!!.getProperty(property))
                        bottom_sheet.visibility=View.VISIBLE

                        getCentreByRegion(LatLang.latLangAlgeria[feature!!.getProperty(property)]!!.id)

                        //set info window by KEY of HASHMAP  "latLangAlgeria[KEY]!!"
                        setInfoWindow(
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
            "global"->{
                layerGeoJson.setOnFeatureClickListener(object: GeoJsonLayer.GeoJsonOnFeatureClickListener {
                    override fun onFeatureClick(feature: Feature?) {

                        val property=feature!!.id
                        GeoJsonMapAnimation(property)
                        bottom_sheet.visibility=View.VISIBLE

                        /*getCentreByRegion(LatLang.latLangAlgeria[property]!!.id)

                        //set info window by KEY of HASHMAP  "latLangAlgeria[KEY]!!"
                        setInfoWindow(
                            LatLang.latLangAlgeria[property]!!.ArabicName,
                            LatLang.latLangAlgeria[property]!!.degre,
                            LatLang.latLangAlgeria[property]!!.confirme.toString(),
                            LatLang.latLangAlgeria[property]!!.critique.toString(),
                            LatLang.latLangAlgeria[property]!!.suspect.toString(),
                            LatLang.latLangAlgeria[property]!!.mort.toString(),
                            LatLang.latLangAlgeria[property]!!.guerie.toString(),
                            hospitals)*/
                    }
                })
            }
        }



    }

    fun setInfoWindow(region_:String,
                      state_:Int,
                      nb_cases_:String,
                      nb_holder_:String,
                      nb_doubtful_:String,
                      nb_deaths_:String,
                      nb_recovred_:String,
                      hospitals:MutableList<Hospital>){
        val recycler_view:RecyclerView=activity.findViewById(R.id.recycler_view)
        var etat:String=""
        region.text=region_
        when(state_){
            0->{etat=context.getString(R.string.danger_lvl1)
                state.setTextColor(Color.parseColor(Util.getProperty("dangerLvl1Color", context!!)))
            }
            1->{etat=context.getString(R.string.danger_lvl2)
                state.setTextColor(Color.parseColor(Util.getProperty("dangerLvl2Color", context!!)))
            }
            2->{etat=context.getString(R.string.danger_lvl3)
                state.setTextColor(Color.parseColor(Util.getProperty("dangerLvl3Color", context!!)));
            }
        }
        state.text=etat
        nb_cases.text=nb_cases_
        nb_holder.text=nb_holder_
        nb_doubtful.text=nb_doubtful_
        nb_deaths.text=nb_deaths_
        nb_recovred.text=nb_recovred_



        val recyclerView: RecyclerView = recycler_view
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

    fun GeoJsonMapAnimation(Property:String){
        //markerList[latLangAlgeria.keys.indexOf(Property)].showInfoWindow()
        if(enterGeoJson){ layerGeoJson.removeLayerFromMap()
        }else{ enterGeoJson=true}
        layerGeoJson= GeoJsonLayer(googleMap, LatLang.latLangAlgeria[Property]!!.kmlResource,context,markerManager,polygonManager,polylineManager, groundOverlayManager)
        val style: GeoJsonPolygonStyle = layerGeoJson.getDefaultPolygonStyle()
        style.setStrokeColor(Color.argb(130,60,40,120))
        style.setStrokeWidth(9F)
        layerGeoJson.addLayerToMap()

    }


}