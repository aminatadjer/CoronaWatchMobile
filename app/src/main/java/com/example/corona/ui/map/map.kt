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

import com.example.corona.R
import com.example.corona.ui.map.LatLang.latLangAlgeria

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
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


class map : Fragment(),OnMapReadyCallback{

    companion object {
        fun newInstance() = map()
    }

    lateinit var bottomSheetBehaivior: BottomSheetBehavior<LinearLayout>

    private lateinit var viewModel: MapViewModel

    private lateinit var mapView:MapView
    private lateinit var mtitel:TextView

    private val PERMISSIONS_REQUEST_ENABLE_GPS=10
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=11
    var mLocationPermissionGranted=false

    lateinit var mapViewModel: MapViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        getStart()
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        mapViewModel=MapViewModel()

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.mapTitle)

        /*val id =  ArrayList<Int>();
        for ( i in 1..10) {
            id.add(resources.getIdentifier("d"+i, "raw", activity!!.packageName))
        }
        */

        SetBottomSheetBehaivior()
        bottom_sheet.visibility=View.GONE

        var mapViewBundle:Bundle?=null
        if(savedInstanceState!=null){
            mapViewBundle=savedInstanceState.getBundle(getString(R.string.MapViewBundleKey))
        }
        mapView=activity!!.findViewById(R.id.map) as MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)


    }


    fun getStart(){
        val context = context // or getBaseContext(), or getApplicationContext()

        var regionList: ArrayList<Region> = arrayListOf()
        val retrofit = Retrofit.Builder()
            .baseUrl(Util.getProperty("baseUrl", context!!))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getAll().enqueue(object: Callback<List<Region>> {
            override fun onResponse(call: Call<List<Region>>, response: retrofit2.Response<List<Region>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Region>? = response.body()
                    if (listBody != null) {
                        regionList.clear()
                        regionList.addAll(listBody)
                    }

                    for (region in regionList){
                        val resId = context!!.resources.getIdentifier("raw/"+region.nom, null, context.packageName)
                        region.kmlResource=resId
                        LatLang.latLangAlgeria.set( region.id.toString() , region)
                    }
                }
            }
            override fun onFailure(call: Call<List<Region>>, t: Throwable) {

            }
        })

    }

    fun SetBottomSheetBehaivior(){
        val linearLayout= activity!!.findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehaivior=BottomSheetBehavior.from(linearLayout)
        bottomSheetBehaivior.setPeekHeight(150)
        bottomSheetBehaivior.setState(BottomSheetBehavior.STATE_EXPANDED)
        bottomSheetBehaivior.setBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState==BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheet.requestLayout()
                }
            }
        })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle=outState.getBundle(getString(R.string.MapViewBundleKey))
        if(mapViewBundle==null){
            mapViewBundle= Bundle()
            outState.putBundle(getString(R.string.MapViewBundleKey),mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }



    override fun onMapReady(googleMap: GoogleMap?) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
             val success = googleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        activity, R.raw.map))
            if (!success) {
                Log.e(TAG, Util.getProperty("StyleParsingMsg", context!!))
            }
        }catch ( e:Resources.NotFoundException) {
            Log.e(TAG, Util.getProperty("StyleNotFound", context!!), e)
        }


        googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom( LatLng(28.0339, 1.6596), 5.0f))

        Thread{
            activity!!.runOnUiThread {
                mapViewModel.setMap(googleMap!!,context!!,activity!!)
            }
        }.start()

    }



    override fun onPause() {
        getStart()
        mapView.onPause()
        super.onPause()
    }
    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }
    override fun onStart() {
        getStart()
        super.onStart()
        mapView.onStart()
        if(checkMapServices()){
            if(mLocationPermissionGranted){

                mapView.onResume()
            }
            else{
                getLocationPermission()}
        }

    }

    override fun onStop() {
        getStart()
        super.onStop()
        mapView.onStop()
    }
    override fun onResume() {
        getStart()
        super.onResume()


        if(checkMapServices()){
            if(mLocationPermissionGranted){

                mapView.onResume()
            }
            else{
                getLocationPermission()}
        }
    }


    fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true
            }
        }
        return false
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage(getString(R.string.gpsPermissionMsg))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                val enableGpsIntent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun isMapsEnabled(): Boolean {
        val manager =activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    fun getLocationPermission() { /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true

        } else {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun isServicesOK(): Boolean {
        Log.d(ContentValues.TAG, Util.getProperty("checkGoogleServiceVersion", context!!))
        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if (available == ConnectionResult.SUCCESS) { //everything is fine and the user can make map requests
            Log.d(ContentValues.TAG, Util.getProperty("checkGoogleServiceWorking", context!!))
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) { //an error occured but we can resolve it
            Log.d(ContentValues.TAG, Util.getProperty("checkGoogleServiceError", context!!))
            val dialog: Dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(activity, available, 1)
            dialog.show()
        } else {
            Toast.makeText(activity, Util.getProperty("errorMapRequest", context!!), Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(ContentValues.TAG, "onActivityResult: called.")
        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {

                } else {
                    getLocationPermission()
                }
            }
        }

    }



}
