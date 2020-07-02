package com.example.corona.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.corona.R


import kotlin.collections.ArrayList


class UserVideo : Fragment() {

     var  mRecyclerView:VideoPlayerRecyclerView?=null
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

        mRecyclerView = activity!!.findViewById(R.id.recycler_view_user_video);

        initRecyclerView()


    }


    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.layoutManager = layoutManager
        val itemDecorator = VerticalSpacingItemDecorator(10)
        mRecyclerView!!.addItemDecoration(itemDecorator)
        val mediaObjects: ArrayList<MediaObject> = arrayListOf()
        for(item in Resources.MEDIA_OBJECTS){
            mediaObjects.add(item)
        }

        mRecyclerView!!.setMediaObjects(mediaObjects)
        val adapter =
            VideoPlayerRecyclerAdapter(mediaObjects, initGlide()!!)
        mRecyclerView!!.adapter = adapter
    }

    private fun initGlide(): RequestManager? {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.white_background)
            .error(R.drawable.white_background)
        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

     override fun onDestroy() {
        if (mRecyclerView != null) mRecyclerView!!.releasePlayer()
        super.onDestroy()

    }
}
