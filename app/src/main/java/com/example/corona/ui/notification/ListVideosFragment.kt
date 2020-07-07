package com.example.corona.ui.notification

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
import com.example.corona.ui.notification.adapter.NotifAdapter
import com.example.corona.ui.notification.entity.Notif
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


    private lateinit var adapter: NotifAdapter



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
            NotifAdapter(requireActivity())
        recyclerView.adapter = adapter

        val ll = ArrayList<Notif>()
        adapter.setVideos(ll)
        val context = context // or getBaseContext(), or getApplicationContext()
        val retrofit = Retrofit.Builder()
            .baseUrl(Util.getProperty("baseUrl", context!!))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create<Service>(Service::class.java)
        service.getAll().enqueue(object: Callback<List<Notif>> {
            override fun onResponse(call: Call<List<Notif>>, response: retrofit2.Response<List<Notif>>?) {
                if ((response != null) && (response.code() == 200)) {
                    val listBody:List<Notif>? = response.body()
                    if (listBody != null) {
                        ll.clear()
                        ll.addAll(listBody)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<List<Notif>>, t: Throwable) {

            }
        })

        adapter.setVideos(ll)

    }


}
