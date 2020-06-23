package com.example.corona.ui

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


class MainActivity : AppCompatActivity (), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    lateinit var navController: NavController
    lateinit var   bottom_bar: SmoothBottomBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


        bottom_bar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                when(pos){
                    0->navController.navigate(R.id.mapFragment)
                    1->navController.navigate(R.id.reportFragment)
                    2->navController.navigate(R.id.postFragment)
                    3->navController.navigate(R.id.videoFragment)
                    4->navController.navigate(R.id.viewFragment)
                    else -> "Number too high"
                }
            }
        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_map -> {
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
                navController.navigate(R.id.videoFragment)
                bottom_bar.setActiveItem(3)
            }
            R.id.nav_view -> {
                navController.navigate(R.id.viewFragment)
                bottom_bar.setActiveItem(4)

            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }




    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.label){
                    "view"->bottom_bar.setActiveItem(4)
                    "video"->bottom_bar.setActiveItem(3)
                    "post"->bottom_bar.setActiveItem(2)
                    "report"->bottom_bar.setActiveItem(1)
                    "map"->bottom_bar.setActiveItem(0)
                }

            }

            //Toast.makeText(this,navController.,Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,null)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        //navController.navigate(R.id.mapFragment)
        //bottom_bar.setActiveItem(0)

        Runtime.getRuntime().gc()
    }


}
