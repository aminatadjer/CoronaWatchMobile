package com.example.corona.ui.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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


        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)




       /* FacebookSdk.setApplicationId(resources.getString(R.string.facebook_app_id))
        FacebookSdk.sdkInitialize(context)
        mFacebook=FacebookHelper(this)

        loginFacebook.setOnClickListener {
            mFacebook.performSignIn(this)
        }*/
        // TODO: Use the ViewModel

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= ""

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
