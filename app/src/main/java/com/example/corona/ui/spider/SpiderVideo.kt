package com.example.corona.ui.spider

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R
import com.example.corona.ui.post.NetworkConnection
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.corona.ui.Util
import com.example.corona.ui.map.LatLang

import com.example.corona.ui.spider.Service

import kotlinx.android.synthetic.main.fragment_spider_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


class SpiderVideo : Fragment() {

    private lateinit var mtitel:TextView
    var ll: MutableList<Publication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_spider_video, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStart()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.mapTitle)

        val networkConnection= NetworkConnection(context!!)
        networkConnection.observe(this, Observer {isConnected->
            if (isConnected){
                recycler_view_spider.visibility=View.VISIBLE
                disconected_view_spider.visibility=View.GONE
                spiderVideoFragment.setBackgroundColor(Color.parseColor("#59CFCCCC"))

                val recyclerViewSpider: RecyclerView = recycler_view_spider as RecyclerView
                recyclerViewSpider.setHasFixedSize(true)
                recyclerViewSpider.layoutManager = LinearLayoutManager(activity)
                recyclerViewSpider.setHasFixedSize(true)

                val adapter = VideoSpiderAdapter()


                //required setUrl


                // add here

                adapter.setVideoSpider(ll)
                recyclerViewSpider.adapter = adapter

                adapter.SetOnItemClickListner(object :
                    VideoSpiderAdapter.OnItemClickListner {
                    override fun onItemClick(spiderItem: Publication) {

                        val nextAction= SpiderVideoDirections.actionSpiderVideoFragmentToSpiderPageFragment()
                        nextAction.setUrl(spiderItem.url)
                        Navigation.findNavController(view!!).navigate(nextAction)
                    }

                })

            }else{
                recycler_view_spider.visibility=View.GONE
                disconected_view_spider.visibility=View.VISIBLE
                spiderVideoFragment.setBackgroundColor(Color.WHITE)
            }

        })


    }

    fun getStart(){
        val context = context // or getBaseContext(), or getApplicationContext()
        val retrofit = Retrofit.Builder()
            .baseUrl(Util.getProperty("baseUrl", context!!))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getAll().enqueue(object: Callback<List<Publication>> {
            override fun onResponse(call: Call<List<Publication>>, response: retrofit2.Response<List<Publication>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Publication>? = response.body()
                    if (listBody != null) {
                        ll.clear()
                        ll.addAll(listBody)
                    }
                }
            }
            override fun onFailure(call: Call<List<Publication>>, t: Throwable) {

            }
        })

    }




}
