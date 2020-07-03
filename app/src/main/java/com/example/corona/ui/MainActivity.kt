package com.example.corona.ui

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.corona.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar
//import java.util.jar.Manifest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
//import android.location.Location
import android.location.LocationListener
//import android.location.LocationManager
import android.os.Build
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
import android.provider.Settings
import android.util.Log
//import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import android.app.*
//import android.content.Context
//import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
//import android.os.Build
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.abs


private const val PERMISSION_REQUEST = 10


class MainActivity : AppCompatActivity (), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    lateinit var navController: NavController
    lateinit var   bottom_bar: SmoothBottomBar


    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.corona.ui"
    private val description = "Test notification"

    fun xyz(){
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var mutableList = mutableListOf(mutableListOf(46.49,4.85),mutableListOf(56.49,6.85))
        //var x = 36.49

        var y = 2.85

        for(item in mutableList) {
            if (distance(item[0],item[1],locationNetwork!!.latitude,locationNetwork!!.longitude)<1.0) {
                val intent = Intent(this, MainActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                val contentView = RemoteViews(packageName, R.layout.notification_layout)
                contentView.setTextViewText(R.id.tv_title, "coronawatch notification")
                contentView.setTextViewText(R.id.tv_content, "zone de risque")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(
                        channelId,
                        description,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_round)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.ic_launcher
                            )
                        )
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.ic_launcher_round)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                this.resources,
                                R.drawable.ic_launcher
                            )
                        )
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                enableView()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            enableView()
        }


        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                xyz()
                mainHandler.postDelayed(this, 60000)
            }
        })


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)



        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navController=Navigation.findNavController(this,R.id.nav_host_fragment)

        // bottom_bar.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        bottom_bar=this.findViewById(R.id.bottom_bar)
        bottom_bar.setActiveItem(2)


        bottom_bar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {

                when(pos){
                    0->{

                        navController.navigate(R.id.mapFragment)


                    }
                    1->{

                        navController.navigate(R.id.reportFragment)

                    }
                    2->{

                        navController.navigate(R.id.postFragment)

                    }
                    3->{

                        navController.navigate(R.id.spiderVideoFragment)

                    }
                    4->{

                        navController.navigate(R.id.userVideoFragment)

                    }
                    else -> "Number too high"
                }
                Runtime.getRuntime().gc()
            }
        })
    }



    private fun enableView() {

        getLocation()
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
                            //tv_result.append("\nGPS ")
                            //tv_result.append("\nLatitude : " + locationGps!!.latitude)
                            //tv_result.append("\nLongitude : " + locationGps!!.longitude)
                            Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                            Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            //tv_result.append("\nNetwork ")
                            //tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                            //tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                            Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                            Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    //tv_result.append("\nNetwork ")
                    //tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                    //tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                    Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                    Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                }else{
                    //tv_result.append("\nGPS ")
                    //tv_result.append("\nLatitude : " + locationGps!!.latitude)
                    //tv_result.append("\nLongitude : " + locationGps!!.longitude)
                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun distance(lat1:Double, lon1:Double, lat2:Double, lon2:Double):Double {
        var theta:Double = lon1 - lon2
        var dist:Double = Math.sin(deg2rad(lat1))* Math.sin(deg2rad(lat2))+ Math.cos(deg2rad(lat1))* Math.cos(deg2rad(lat2))* Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return (dist)
    }

    private  fun deg2rad(deg: Double):Double {
        return (deg * Math.PI / 180.0)
    }

    private fun rad2deg(rad: Double):Double {
        return (rad * 180.0 / Math.PI)
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                enableView()

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapFragment -> {

                navController.navigate(R.id.mapFragment)
                bottom_bar.setActiveItem(0)

            }
            R.id.postFragment -> {

                navController.navigate(R.id.reportFragment)
                bottom_bar.setActiveItem(1)

            }
            R.id.loginFragment -> {

                navController.navigate(R.id.postFragment)
                bottom_bar.setActiveItem(2)

            }
            R.id.spiderVideoFragment -> {

                navController.navigate(R.id.spiderVideoFragment)
                bottom_bar.setActiveItem(3)

            }
            R.id.userVideoFragment -> {

                navController.navigate(R.id.userVideoFragment)
                bottom_bar.setActiveItem(4)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        Runtime.getRuntime().gc()
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.label){
                    "fragment_user_video"->bottom_bar.setActiveItem(4)
                    "fragment_spider_video"->bottom_bar.setActiveItem(3)
                    "post"->bottom_bar.setActiveItem(2)
                    "report"->bottom_bar.setActiveItem(1)
                    "map"->{

                        bottom_bar.setActiveItem(0)
                    }
                }
                Runtime.getRuntime().gc()

            }

            //Toast.makeText(this,"here",Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        //navController.navigate(R.id.mapFragment)
        //bottom_bar.setActiveItem(0)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.label){
                "fragment_user_video"->bottom_bar.setActiveItem(4)
                "fragment_spider_video"->bottom_bar.setActiveItem(3)
                "post"->bottom_bar.setActiveItem(2)
                "report"->bottom_bar.setActiveItem(1)
                "map"->bottom_bar.setActiveItem(0)
            }

        }
        Runtime.getRuntime().gc()
    }

}
