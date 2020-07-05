package com.example.corona.ui.video

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.corona.R
import com.example.corona.ui.Util
import com.example.corona.ui.spider.Publication


class UserVideoAdapter: RecyclerView.Adapter<UserVideoAdapter.UserVideorHolder>() {

    companion object{
        private var userItemList:List<Video> =ArrayList()

    }


    class UserVideorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val  web_view: WebView =itemView.findViewById(R.id.web_view_video_user_video)

        internal val title:TextView=itemView.findViewById(R.id.title_user_video)
        internal val date:TextView=itemView.findViewById(R.id.date_user_video)
        init {


            web_view.setWebChromeClient(WebChromeClient())
            web_view.setWebViewClient(WebViewClient())
            web_view.getSettings().setAppCacheEnabled(true)
            web_view.getSettings().setJavaScriptEnabled(true)

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserVideorHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_user_item,parent,false)

        return UserVideorHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return userItemList.size
    }

    fun setUserVideo(spiderItemListt:List<Video>)
    {
        userItemList =spiderItemListt
        notifyDataSetChanged()
    }

    // @SuppressLint("SetJavaScriptEnabled")
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: UserVideorHolder, position: Int) {
        val currentSpiderItemList: Video = userItemList.get(position)
        val map :Map<String,String> = mapOf(Pair("text/html","utf-8"))
        holder.web_view.loadUrl("http://192.168.1.9:8000"+currentSpiderItemList.media,map)
        holder.title.text=currentSpiderItemList.commentaire
        holder.date.text=currentSpiderItemList.date


    }





}