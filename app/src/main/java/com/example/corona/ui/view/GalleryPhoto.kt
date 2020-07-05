package com.example.corona.ui.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.corona.R
import kotlinx.android.synthetic.main.fragment_gallery_photo.*
import java.io.ByteArrayOutputStream


class GalleryPhoto : Fragment() {

    val CAMERA_REQUEST = 2
    private var selctedPhoto: Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sendImage_diagno.visibility=View.GONE

        dispatchGalleryPictureIntent()
    }

    fun dispatchGalleryPictureIntent() {
        val galleryIntent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE)


        startActivityForResult(galleryIntent, CAMERA_REQUEST)
    }
    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                selctedPhoto= getImageUri(context!!,data!!.extras!!.get("data") as Bitmap)

                takenImage_diagno.setImageURI(selctedPhoto) // handle chosen image

                sendImage_diagno.visibility=View.VISIBLE

                sendImage_diagno.setOnClickListener{

                }



            }catch (e:ConcurrentModificationException)
            {
                Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
            }

        }

    }

}
