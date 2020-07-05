package com.example.corona.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R
import kotlinx.android.synthetic.main.list_videos_fragment.*



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

        val recyclerView: RecyclerView = list_video as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = VideoAdapter(requireActivity())
        recyclerView.adapter = adapter
/*
        var video1 = Video("1","1","1")
        var video2 = Video("2","2","2")
        var video3 = Video("3","3","3")

        var videos = ArrayList<Video>(video1,video2,video3)
        adapter.setVideos(videos)
 */

        var video1 = Video("قناة العربية", "بث حي ومباشر لقناة سكاي نيوز عربية مجانا عبر الإنترنت ومتوفر على جميع المنصات والأجهزة. شاهد البث أينما كنت على مدار", "1-1-2020", 0)
        var video2 = Video("قناة العربية", "بث حي ومباشر لقناة سكاي نيوز عربية مجانا عبر الإنترنت ومتوفر على جميع المنصات والأجهزة. شاهد البث أينما كنت على مدار", "1-1-2020", 1)
        var video3 = Video("قناة العربية", "بث حي ومباشر لقناة سكاي نيوز عربية مجانا عبر الإنترنت ومتوفر على جميع المنصات والأجهزة. شاهد البث أينما كنت على مدار", "1-1-2020", 2)
        var video4 = Video("قناة العربية", "بث حي ومباشر لقناة سكاي نيوز عربية مجانا عبر الإنترنت ومتوفر على جميع المنصات والأجهزة. شاهد البث أينما كنت على مدار", "1-1-2020", 0)
        var video5 = Video("قناة العربية", "بث حي ومباشر لقناة سكاي نيوز عربية مجانا عبر الإنترنت ومتوفر على جميع المنصات والأجهزة. شاهد البث أينما كنت على مدار", "1-1-2020", 1)



        val arrayList = ArrayList<Video>()
        arrayList.add(video1)
        arrayList.add(video2)
        arrayList.add(video3)
        arrayList.add(video4)
        arrayList.add(video5)

        adapter.setVideos(arrayList)

    }


}
