package com.example.corona.ui.spider.adapter

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
import com.example.corona.ui.spider.entity.Publication


class VideoSpiderAdapter: RecyclerView.Adapter<VideoSpiderAdapter.VideoSpiderHolder>() {

    companion object{
        private var spiderItemList:List<Publication> =ArrayList()
        lateinit var listner: OnItemClickListner
    }


    class VideoSpiderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val  web_view: WebView =itemView.findViewById(R.id.web_view_video_spider)
        internal val  title_spider: TextView =itemView.findViewById(R.id.title_spider)
        internal val  subTitle_spider: TextView =itemView.findViewById(R.id.subTitle_spider)
        internal val progressBar_loading_spider: LottieAnimationView =itemView.findViewById(R.id.progressBar_loading_spider)
        internal val  source_item: ImageView =itemView.findViewById(R.id.source_item)
        internal val layout:LinearLayout=itemView.findViewById(R.id.layout)
        init {

            itemView.setOnClickListener(View.OnClickListener {
                val position=adapterPosition
                if(position!= RecyclerView.NO_POSITION){

                    listner.onItemClick(
                        spiderItemList.get(position))
                }
            })

            web_view.setWebChromeClient(WebChromeClient())
            web_view.setWebViewClient(WebViewClient())
            web_view.getSettings().setAppCacheEnabled(true)
            web_view.getSettings().setJavaScriptEnabled(true)

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoSpiderHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_spider_item,parent,false)

        return VideoSpiderHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return spiderItemList.size
    }

    fun setVideoSpider(spiderItemListt:List<Publication>)
    {
        spiderItemList =spiderItemListt
        notifyDataSetChanged()
    }

   // @SuppressLint("SetJavaScriptEnabled")
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: VideoSpiderHolder, position: Int) {
        val currentSpiderItemList: Publication = spiderItemList.get(position)

       if(!currentSpiderItemList.url.contains("twitter.com")){
           if(currentSpiderItemList.url.contains("www.youtube")){
               holder.source_item.setImageResource(R.drawable.ic_youtube)
           }else{
               holder.source_item.setImageResource(R.drawable.gmail)
           }

           /*holder.web_view.webViewClient= object :WebViewClient(){
               override fun shouldOverrideUrlLoading(
                   view: WebView?,
                   url: String
               ): Boolean {
                   holder.progressBar_loading_spider.visibility=View.VISIBLE
                   view!!.loadUrl(currentSpiderItemList.url)
                   return true
               }

               override fun onPageFinished(view: WebView?, url: String?) {
                   holder.progressBar_loading_spider.visibility=View.GONE
               }
           }*/
           val map :Map<String,String> = mapOf(Pair("text/html","utf-8"))
           holder.web_view.loadUrl(currentSpiderItemList.url,map)
       }else{
           holder.progressBar_loading_spider.visibility=View.GONE
           holder.source_item.setImageResource(R.drawable.ic_twitter)

           //holder.layout.removeView(holder.web_view)
           //holder.layout.addView(holder.web_view,LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

           holder.web_view.setOnTouchListener(object :View.OnTouchListener{
               override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                   return true
               }
           })

           holder.web_view.loadDataWithBaseURL("https://twitter.com","<blockquote class=\"twitter-tweet\">\n" +
                   "   <p lang=\"en\" dir=\"ltr\"> <a href=\"https://t.co/sAcZtUUtB8\"></a></p>\n" +
                   " <a href=\""+currentSpiderItemList.url+"\"></a>\n" +
                   "</blockquote>\n" +
                   "<script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>","text/html","utf-8","")
       }



       holder.title_spider.text=currentSpiderItemList.titre
       holder.subTitle_spider.text=currentSpiderItemList.description
    }


    interface OnItemClickListner{
        fun onItemClick(spiderItem: Publication)
    }

    fun SetOnItemClickListner(listnerr: OnItemClickListner){
        listner =listnerr
    }


}
