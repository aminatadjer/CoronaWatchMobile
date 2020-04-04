package com.example.corona.ui.report

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController

import com.example.corona.R
import kotlinx.android.synthetic.main.galery_fragment.*
import kotlinx.android.synthetic.main.takenvideo_fragment.*

class takenvideo : Fragment() {

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

        textFieldVideo.visibility=View.GONE
        sendVideo.visibility=View.GONE
        dispatchTakeVideoIntent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            val videoUri: Uri? = data!!.data

            video_view.setVideoURI(videoUri)

            video_view!!.requestFocus()
            val mediactrl=MediaController(context)
            video_view.setMediaController(mediactrl)
            mediactrl.setAnchorView(video_view)
            video_view!!.start()
            textFieldVideo.visibility=View.VISIBLE
            sendVideo.visibility=View.VISIBLE
        }

    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)


        }
    }

}
