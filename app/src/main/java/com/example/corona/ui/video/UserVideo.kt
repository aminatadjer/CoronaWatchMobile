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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.corona.R
import com.example.corona.ui.post.NetworkConnection
import com.example.corona.ui.report.photo.reportDirections
import com.example.corona.ui.spider.VideoSpiderAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_spider_video.*
import kotlinx.android.synthetic.main.fragment_user_video.*


import kotlin.collections.ArrayList


class UserVideo : Fragment() {

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
        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.mapTitle)



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

                ll.add(Video(1,"https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4"))
                ll.add(Video(2,"https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4"))
                ll.add(Video(3,"https://s3.ca-central-1.amazonaws.com/codingwithmitch/media/VideoPlayerRecyclerView/Sending+Data+to+a+New+Activity+with+Intent+Extras.mp4"))

                adapter.setUserVideo(ll)
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
