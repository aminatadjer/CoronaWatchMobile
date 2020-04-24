package com.example.corona.ui.report.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import kotlinx.android.synthetic.main.galery_fragment.*


class galery : Fragment() {

    companion object {
        fun newInstance() = galery()
    }
    val REQUEST_GALLERY_CAPTURE = 2

    private var selctedImage:Uri?=null

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
        textFieldImageGallery.bringToFront()

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.toolbar_msg)
        textFieldImageGallery.visibility=View.GONE
        sendImage.visibility=View.GONE
        dispatchGalleryPictureIntent()

        sendImage.setOnClickListener {
            Toast.makeText(context,textFieldImageGallery.editText?.text.toString(),Toast.LENGTH_LONG).show()
        }
        activity!!.applicationContext

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_CAPTURE){


            //val con=data!!.data


            selctedImage=data?.data




           // val r: String? =con!!.path


            //
            takenImage.setImageURI(selctedImage) // handle chosen image



            textFieldImageGallery.visibility=View.VISIBLE
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
