package com.example.corona.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.corona.R
import com.example.corona.ui.Util
import com.example.corona.ui.post.entity.Article

class ArticleAdapter(contextt: Context) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    companion object{
        private var articleList:List<Article> =ArrayList()
        lateinit var listner: OnItemClickListner
    }
     var context:Context = contextt


    class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val  web_view: WebView =itemView.findViewById(R.id.web_view)
        internal val  title: TextView =itemView.findViewById(R.id.title)
        internal val  subTitle: TextView =itemView.findViewById(R.id.subTitle)
        internal val  coment: TextView =itemView.findViewById(R.id.coment)
        internal val  like: TextView =itemView.findViewById(R.id.like)
        internal val progressBar_loading: LottieAnimationView =itemView.findViewById(R.id.progressBar_loading)
        //internal val  read: Button =itemView.findViewById(R.id.read_btn)

        init {
            itemView.setOnClickListener(View.OnClickListener {
                val position=adapterPosition
                if(position!= RecyclerView.NO_POSITION){

                    listner.onItemClick(
                        articleList.get(position))
                }
            })
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item,parent,false)

        return ArticleHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    fun setArticle(articleListt:List<Article>)
    {
        articleList =articleListt
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val currentArticle: Article = articleList.get(position)
        holder.web_view.webViewClient= object :WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String
            ): Boolean {
                holder.progressBar_loading.visibility=View.VISIBLE
                view!!.loadUrl(Util.getProperty("baseUrl2", context)+currentArticle.url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                holder.progressBar_loading.visibility=View.GONE
            }
        }


            holder.title.text = currentArticle.titre
            holder.subTitle.text = currentArticle.date
            holder.coment.text = "38"
            holder.like.text = "12"
            holder.web_view.loadUrl(Util.getProperty("baseUrl2", context) + currentArticle.url)



    }

    interface OnItemClickListner{
        fun onItemClick(article: Article)
    }

    fun SetOnItemClickListner(listnerr: OnItemClickListner){
        listner =listnerr
    }

    fun getAricleAt(position: Int): Article? {
        return articleList[position]
    }

}