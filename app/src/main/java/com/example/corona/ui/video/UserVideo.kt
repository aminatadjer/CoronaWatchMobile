package com.example.corona.ui.video

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.corona.R
import com.example.corona.ui.MainActivity
import com.example.corona.ui.Util
import com.example.corona.ui.post.NetworkConnection
import com.example.corona.ui.report.photo.reportDirections

import com.example.corona.ui.video.Service

import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_spider_video.*
import kotlinx.android.synthetic.main.fragment_user_video.*
import me.ibrahimsn.lib.SmoothBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


import kotlin.collections.ArrayList


class UserVideo : Fragment() {

    lateinit var toolbar: SmoothBottomBar
    var ll: MutableList<Video> = ArrayList()
    private lateinit var mtitel: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_video, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!MainActivity.conected){
            Toast.makeText(context,"not conected", Toast.LENGTH_LONG).show()
            MainActivity.navController.navigate(R.id.loginFragmentGmail)
        }
        else{

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.userVideoTitle)
        tolb.visibility=View.VISIBLE

        toolbar = activity!!.findViewById(R.id.bottom_bar)
        toolbar.visibility=View.VISIBLE



        val networkConnection= NetworkConnection(context!!)
        networkConnection.observe(this, Observer {isConnected->
            if (isConnected) {
                recycler_view_user_video.visibility = View.VISIBLE
                disconected_view_user_video.visibility = View.GONE
                userVideoFragment.setBackgroundColor(Color.parseColor("#59CFCCCC"))

                val recyclerViewUser: RecyclerView = recycler_view_user_video as RecyclerView
                recyclerViewUser.setHasFixedSize(true)
                recyclerViewUser.layoutManager = LinearLayoutManager(activity)
                recyclerViewUser.setHasFixedSize(true)

                val adapter = UserVideoAdapter()


                //required setUrl

                adapter.setUserVideo(ll)
                val context = context // or getBaseContext(), or getApplicationContext()
                val retrofit = Retrofit.Builder()
                    .baseUrl(Util.getProperty("baseUrl", context!!))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create<Service>(Service::class.java)
                service.getAll().enqueue(object: Callback<List<Video>> {
                    override fun onResponse(call: Call<List<Video>>, response: retrofit2.Response<List<Video>>?) {
                        if ((response != null) && (response.code() == 200)) {
                            val listBody:List<Video>? = response.body()
                            if (listBody != null) {
                                ll.clear()
                                ll.addAll(listBody)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                    override fun onFailure(call: Call<List<Video>>, t: Throwable) {

                    }
                })
                recyclerViewUser.adapter = adapter
            }else{
                recycler_view_user_video.visibility=View.GONE
                disconected_view_user_video.visibility=View.VISIBLE
                userVideoFragment.setBackgroundColor(Color.WHITE)
            }

        })

        val add_video=activity!!.findViewById<FloatingActionButton>(R.id.add_video)

        add_video.setOnClickListener {

            val takenvideoAction =UserVideoDirections.actionUserVideoFragmentToGalleryVideo()
            Navigation.findNavController(it).navigate(takenvideoAction)
        }
    }

    }








}
