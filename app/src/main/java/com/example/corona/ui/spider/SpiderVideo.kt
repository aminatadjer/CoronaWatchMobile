package com.example.corona.ui.spider

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R
import com.example.corona.ui.post.NetworkConnection
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import kotlinx.android.synthetic.main.fragment_spider_video.*
import java.net.URL


class SpiderVideo : Fragment() {

    private lateinit var mtitel:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(context,"once",Toast.LENGTH_LONG).show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spider_video, container, false)
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

                var ll: MutableList<SpiderItem> = ArrayList()
                ll.add(
                    SpiderItem(
                        setUrl("https://www.youtube.com/watch?v=z44CLCafepA&fbclid=IwAR2DxFgiPcs-BVnIt2fInAmhza41hIedEwU5SWjQ8d8Y_yUQ6rWfrlRu6Oc"),
                        "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                        "المصدر: دبي - العربية.نت"
                    )
                )

                ll.add(
                    SpiderItem(
                        setUrl("https://twitter.com/CDCgov/status/1276254982782832644"),
                        "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                        "المصدر: دبي - العربية.نت"
                    )
                )

                ll.add(
                    SpiderItem(
                        setUrl("https://twitter.com/marcomh20/status/1275547023865831424?ref_src=twsrc%5Etfw"),
                        "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                        "المصدر: دبي - العربية.نت"
                    )
                )
                ll.add(
                    SpiderItem(
                        setUrl("https://www.youtube.com/watch?v=svdq1BWl4r8"),
                        "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                        "المصدر: دبي - العربية.نت"
                    )
                )

                adapter.setVideoSpider(ll)
                recyclerViewSpider.adapter = adapter

                adapter.SetOnItemClickListner(object :
                    VideoSpiderAdapter.OnItemClickListner {
                    override fun onItemClick(spiderItem: SpiderItem) {

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



    fun setUrl(url:String): String {
        if(url.contains("www.youtube")){
            return "https://www.youtube.com/embed/"+extractYoutubeId(url)
        }
        else{
            return url
        }
    }

     fun extractYoutubeId(url: String): String? {
        var id: String? = null
        try {
            val query: String = URL(url).getQuery()
            if (query != null) {
                val param = query.split("&").toTypedArray()
                for (row in param) {
                    val param1 = row.split("=").toTypedArray()
                    if (param1[0] == "v") {
                        id = param1[1]
                    }
                }
            } else {
                if (url.contains("embed")) {
                    id = url.substring(url.lastIndexOf("/") + 1)
                }
            }
        } catch (ex: Exception) {
            Log.e("Exception", ex.toString())
        }
        return id
    }

}
