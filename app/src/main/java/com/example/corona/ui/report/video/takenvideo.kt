package com.example.corona.ui.report.video

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import kotlinx.android.synthetic.main.takenvideo_fragment.*


class takenvideo : Fragment(){

    val REQUEST_VIDEO_CAPTURE = 3
    companion object {
        fun newInstance() = takenvideo()
    }

    private lateinit var viewModel: TakenvideoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.takenvideo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TakenvideoViewModel::class.java)
        // TODO: Use the ViewModel

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        //val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        //mtitel.text= getString(R.string.reportTitle)
        textFieldVideo.visibility=View.GONE
        sendVideo.visibility=View.GONE
        try {
            dispatchTakeVideoIntent()
        }catch (e:ConcurrentModificationException)
        {
            Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
        }

        sendVideo.setOnClickListener {
            if(videoDuriationSecond<11)
            {
                //allowe ipload
            }
            else{
                Toast.makeText(context!!,"يجب الا تتجاوز مدة الفيديو 15 ثانية اعد المحاولة",Toast.LENGTH_LONG).show()
            }
        }


    }
    var  videoUri: Uri? =null
    var videoDuriationSecond:Long=0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {

            try {
                videoUri= data!!.data

                val retriever=MediaMetadataRetriever()
                retriever.setDataSource(context,videoUri)

                val time=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                 videoDuriationSecond=time.toLong()/1000
                retriever.release()
                //Toast.makeText(context!!,videoDuriationSecond.toString(),Toast.LENGTH_LONG).show()


                video_view.setVideoURI(videoUri)

                video_view!!.requestFocus()
                val mediactrl=MediaController(context)
                video_view.setMediaController(mediactrl)
                mediactrl.setAnchorView(video_view)
                video_view!!.start()

                textFieldVideo.visibility=View.VISIBLE
                sendVideo.visibility=View.VISIBLE
            }catch (e:ConcurrentModificationException)
            {
                Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
        }
    }





}
