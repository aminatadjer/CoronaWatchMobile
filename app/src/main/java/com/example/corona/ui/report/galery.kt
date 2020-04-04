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

import com.example.corona.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.galery_fragment.*
import kotlinx.android.synthetic.main.takenvideo_fragment.*

class galery : Fragment() {

    companion object {
        fun newInstance() = galery()
    }
    val REQUEST_GALLERY_CAPTURE = 2

    private lateinit var viewModel: GaleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.galery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GaleryViewModel::class.java)
        // TODO: Use the ViewModel


        textFieldImage.visibility=View.GONE
        sendImage.visibility=View.GONE
        dispatchGalleryPictureIntent()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_CAPTURE){


            takenImage.setImageURI(data?.data) // handle chosen image
            textFieldImage.visibility=View.VISIBLE
            sendImage.visibility=View.VISIBLE

        }

    }

    fun dispatchGalleryPictureIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, REQUEST_GALLERY_CAPTURE)
    }

}
