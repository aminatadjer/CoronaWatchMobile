package com.example.corona.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.corona.R
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : AppCompatActivity () {


         lateinit var navController: NavController

    lateinit var   bottom_bar: SmoothBottomBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    1->navController.navigate(R.id.postFragment)
                    2->navController.navigate(R.id.reportFragment)
                    3->navController.navigate(R.id.videoFragment)
                    else -> "Number too high"
                }
            }
        })




    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when(destination.label){
                    "video"->bottom_bar.setActiveItem(3)
                    "report"->bottom_bar.setActiveItem(2)
                    "post"->bottom_bar.setActiveItem(1)
                    "map"->bottom_bar.setActiveItem(0)
                }

            }

            //Toast.makeText(this,navController.,Toast.LENGTH_LONG).show()
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

        Runtime.getRuntime().gc()
    }
}
