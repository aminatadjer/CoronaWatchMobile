package com.example.corona.ui.report.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import com.example.corona.ui.Util

import com.example.corona.ui.upload.UploadImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.galery_fragment.*
import kotlinx.android.synthetic.main.takenvideo_fragment.*
import java.io.IOException


class galery : Fragment() {

    companion object {
        fun newInstance() = galery()

    }
    val REQUEST_GALLERY_CAPTURE = 2
    lateinit var uploader: UploadImage
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
        mtitel.text= getString(R.string.reportTitle)
        textFieldImageGallery.visibility=View.GONE
        sendImage.visibility=View.GONE

        dispatchGalleryPictureIntent()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_CAPTURE){

            selctedImage=data?.data
            takenImage.setImageURI(selctedImage) // handle chosen image
            textFieldImageGallery.visibility=View.VISIBLE
            sendImage.visibility=View.VISIBLE
            sendImage.setOnClickListener{
                if (data != null)
                {   uploader=UploadImage(activity!!)
                    val contentURI = data!!.data
                    val commentaire:String=textFieldImageGallery.editText?.text.toString()
                    val path: String? = contentURI?.let { it1 -> uploader.getRealPathFromURI(it1) }
                    try
                    {
                        if (contentURI != null) {
                            Toast.makeText(activity!!.applicationContext, getString(R.string.correctMsg), Toast.LENGTH_SHORT).show()
                            uploader.uploadImage(path.toString(),commentaire,
                                Util.getProperty("urlReport", context!!))
                        }
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(activity!!.applicationContext, getString(R.string.faildMsg), Toast.LENGTH_SHORT).show()
                    }

                }


            }

        }

    }


    fun dispatchGalleryPictureIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, REQUEST_GALLERY_CAPTURE)
    }



}
