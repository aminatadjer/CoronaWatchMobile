package com.example.corona.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R
import com.example.corona.ui.Util
import com.example.corona.ui.view.adapter.VideoAdapter
import com.example.corona.ui.view.entity.Video
import kotlinx.android.synthetic.main.list_videos_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListVideosFragment : Fragment() {

    companion object {
        fun newInstance() =
            ListVideosFragment()
    }


    private lateinit var adapter: VideoAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_videos_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= "اشعارات"
        tolb.visibility=View.VISIBLE

        val recyclerView: RecyclerView = list_video as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter =
            VideoAdapter(requireActivity())
        recyclerView.adapter = adapter
/*
        var video1 = Video("1","1","1")
        var video2 = Video("2","2","2")
        var video3 = Video("3","3","3")

        var videos = ArrayList<Video>(video1,video2,video3)
        adapter.setVideos(videos)
 */
        val ll = ArrayList<Video>()
        adapter.setVideos(ll)
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

        adapter.setVideos(ll)

    }


}
