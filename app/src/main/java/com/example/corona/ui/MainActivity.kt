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
import com.example.corona.R
import com.google.android.material.navigation.NavigationView
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

import android.app.*
//import android.content.Context
//import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.ImageView
//import android.os.Build
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
import android.widget.RemoteViews
import com.example.corona.ui.map.local.Region
import com.example.corona.ui.map.Service
import com.example.corona.ui.notification.ExampleDialog
import com.example.corona.ui.notification.NotificationCounter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val PERMISSION_REQUEST = 10


class MainActivity : AppCompatActivity (), NavigationView.OnNavigationItemSelectedListener {

    companion object{
        var conected=false
        lateinit var navController: NavController
    }
    var isFragmentOneLoaded = true
    val manager = supportFragmentManager

    //lateinit var button:Button
    lateinit var notificationCounter: NotificationCounter
    lateinit var textView_notification:TextView
    lateinit var imageView_notification:ImageView



    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    lateinit var   bottom_bar: SmoothBottomBar
    lateinit var locationManager: LocationManager

    private var hasNetwork = false
    var i=0
    var morts=0
    var malades=0
    var name=""
    var regionList: ArrayList<Region> = arrayListOf()
    var danger:Boolean=false
    private var locationNetwork: Location? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.corona.ui"
    private val description = "Test notification"

    fun xyz(){
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var mutableList = mutableListOf(mutableListOf(36.49,2.86),mutableListOf(46.49,4.85),mutableListOf(56.49,6.85))
        //var x = 36.49
        val context = this // or getBaseContext(), or getApplicationContext()


        val retrofit = Retrofit.Builder()
            .baseUrl(Util.getProperty("baseUrl", context!!)!!)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getRisked().enqueue(object: Callback<List<Region>> {
            override fun onResponse(call: Call<List<Region>>, response: retrofit2.Response<List<Region>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Region>? = response.body()
                    if (listBody != null) {

                        regionList.addAll(listBody)
                        for (region in regionList){

                                morts = region.mort
                                if(region.degre==2)
                                {danger = true}
                                malades = region.confirme
                                name = region.ArabicName

                        }
                    }

                }
            }
            override fun onFailure(call: Call<List<Region>>, t: Throwable) {
                Toast.makeText(getApplicationContext(),"no",Toast.LENGTH_SHORT).show();

            }
        })

        var y = 2.85

            if (danger) {
                i++
                if(i==1){
                openDialog()}
                val intent = Intent(this, MainActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                val contentView = RemoteViews(packageName, R.layout.notification_layout)
                contentView.setTextViewText(R.id.tv_title, " تنبيه حذار لقد دخلت منطقة خطرة عليك توخ الحذر")
                contentView.setTextViewText(R.id.tv_content,name+ " وفيات"+morts+"حالات"+malades )

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        imageView_notification = findViewById(R.id.notificationIcon)
        textView_notification = findViewById(R.id.notificationNumber)



        //button = findViewById(R.id.button)
        notificationCounter =
            NotificationCounter(findViewById(R.id.bell))
/*
        button.setOnClickListener {
            notificationCounter.increaseNumber()
        }

 */



        imageView_notification.setOnClickListener {
            textView_notification.setText("0")

            notificationCounter.notification_number_counter=0
            navController.navigate(R.id.list_videos_fragment)
        }

    /*    button.setOnClickListener(View.onClickListener(){
            @Override
            fun onClick(view:View){
                notificationCounter.increaseNumber()
            }
        })
        */



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
                    0-> navController.navigate(R.id.mapFragment)
                    1->navController.navigate(R.id.reportFragment)
                    2->navController.navigate(R.id.postFragment)
                    3->navController.navigate(R.id.spiderVideoFragment)
                    4->navController.navigate(R.id.userVideoFragment)
                    else -> "Number too high"
                }

            }
        })
    }


/*
    fun ShowFragmentOne() {
        val transaction = manager.beginTransaction()
        val fragment = ListVideosFragment()
        transaction.replace(R.id.postFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentOneLoaded = true
    }

 */
/*
    fun ShowFragmentTwo() {
        val transaction = manager.beginTransaction()
        val fragment = post()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentOneLoaded = false
    }
 */



    fun openDialog() {
        var exampleDialog : ExampleDialog =
            ExampleDialog()
        exampleDialog.show(getSupportFragmentManager(), "This is a Dialog")
    }

    private fun enableView() {

        getLocation()
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if ( hasNetwork) {


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

            if(locationNetwork!= null){

                    //tv_result.append("\nNetwork ")
                    //tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                    //tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                    Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                    Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)

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
                        Toast.makeText(this, getString(R.string.PermissionDeniedMsg), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.settingsEnableMsg), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                enableView()

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //Toast.makeText(this,"item",Toast.LENGTH_LONG).show()
        when (item.itemId) {
            /*R.id.nav_map -> {
                navController.navigate(R.id.mapFragment)
                bottom_bar.setActiveItem(0)

            }
            R.id.nav_public -> {
                navController.navigate(R.id.reportFragment)
                bottom_bar.setActiveItem(1)
            }
            R.id.nav_warnings -> {
                navController.navigate(R.id.postFragment)
                bottom_bar.setActiveItem(2)
            }
            R.id.nav_video -> {
                navController.navigate(R.id.spiderVideoFragment)
                bottom_bar.setActiveItem(3)
            }*/
            R.id.nav_view -> {
                navController.navigate(R.id.loginFragmentGmail)
            }

            R.id.nav_sante -> {
                navController.navigate(R.id.diagnose_fragment)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
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
