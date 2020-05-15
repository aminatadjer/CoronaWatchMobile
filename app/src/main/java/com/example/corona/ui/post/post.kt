package com.example.corona.ui.post

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle

import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_fragment.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import androidx.camera.extensions.BokehImageCaptureExtender
import androidx.camera.extensions.HdrImageCaptureExtender
import androidx.camera.extensions.ImageCaptureExtender
import androidx.camera.extensions.NightImageCaptureExtender
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class post : Fragment()/*,FacebookListener*/ {


    companion object {
        fun newInstance() = post()


    }


    //private lateinit var mFacebook:FacebookHelper

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.bottom_nav.visibility = View.GONE

        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)


        val networkConnection=NetworkConnection(context!!)
        networkConnection.observe(this, Observer {isConnected->
            if (isConnected){
                recycler_view.visibility=View.VISIBLE
                disconected_view.visibility=View.GONE
                postFragment.setBackgroundColor(Color.parseColor("#59CFCCCC"))

                val recyclerView: RecyclerView = recycler_view as RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.setHasFixedSize(true)

                val adapter = ArticleAdapter()
                recyclerView.adapter = adapter


                var ll: MutableList<Article> = ArrayList()
                ll.add(Article("https://www.bbc.com/arabic/world-52665698",
                    "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                    "المصدر: دبي - العربية.نت",
                    146,
                    85))

                ll.add(Article("https://www.bbc.com/arabic/world-52665698",
                    "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                    "المصدر: دبي - العربية.نت",
                    146,
                    85))

                ll.add(Article("https://www.bbc.com/arabic/world-52665698",
                    "فيروس كورونا: كيف يفحص مطار هونغ كونغ الركاب القادمين ويتابعهم؟",
                    "المصدر: دبي - العربية.نت",
                    146,
                    85))



                adapter.setArticle(ll)

                adapter.SetOnItemClickListner(object : ArticleAdapter.OnItemClickListner {
                    override fun onItemClick(article: Article) {

                        val nextAction=postDirections.actionPostFragmentToArticlePageFragment()
                        nextAction.setUrl(article.url)
                        Navigation.findNavController(view!!).navigate(nextAction)
                    }

                })
            }else{
                recycler_view.visibility=View.GONE
                disconected_view.visibility=View.VISIBLE
                postFragment.setBackgroundColor(Color.WHITE)
            }

        })
        /* FacebookSdk.setApplicationId(resources.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(context)
        mFacebook=FacebookHelper(this)

        loginFacebook.setOnClickListener {
            mFacebook.performSignIn(this)
        }*/
        // TODO: Use the ViewModel



    }
        /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFacebook.onActivityResult(requestCode,resultCode,data)
    }

    override fun onFbSignInSuccess(authToken: String?, userId: String?) {

        Toast.makeText(context,""+ userId,Toast.LENGTH_SHORT).show()
    }

    override fun onFBSignOut() {
        Toast.makeText(context,"sign out",Toast.LENGTH_SHORT).show()
    }

    override fun onFbSignInFail(errorMsg: String?) {
        Toast.makeText(context,""+ errorMsg,Toast.LENGTH_SHORT).show()
    }
*/


}