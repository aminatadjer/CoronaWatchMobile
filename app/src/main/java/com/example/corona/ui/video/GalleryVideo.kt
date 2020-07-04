package com.example.corona.ui.video

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast

import com.example.corona.R
import com.example.corona.ui.upload.UploadImage
import kotlinx.android.synthetic.main.fragment_gallery_video.*
import kotlinx.android.synthetic.main.galery_fragment.*


class GalleryVideo : Fragment() {

    val REQUEST_GALLERY_CAPTURE = 2
    private var selctedVideo: Uri?=null
    lateinit var uploader: UploadImage

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sendVideo_gallery.visibility=View.GONE
        video_view_gallery.visibility=View.GONE
        dispatchGalleryPictureIntent()
    }

    fun dispatchGalleryPictureIntent() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, REQUEST_GALLERY_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GALLERY_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                selctedVideo= data!!.data

                val retriever= MediaMetadataRetriever()
                retriever.setDataSource(context,selctedVideo)

                retriever.release()

                video_view_gallery.setVideoURI(selctedVideo)

                video_view_gallery!!.requestFocus()
                val mediactrl= MediaController(context)
                video_view_gallery.setMediaController(mediactrl)
                mediactrl.setAnchorView(video_view_gallery)
                video_view_gallery!!.start()


                video_view_gallery.visibility=View.VISIBLE
                sendVideo_gallery.visibility=View.VISIBLE
                sendVideo_gallery.setOnClickListener{
                    if (data != null)
                    {   uploader= com.example.corona.ui.upload.UploadImage(activity!!)
                        val contentURI = selctedVideo
                        val commentaire: kotlin.String =textFieldTitre.editText?.text.toString()
                        val path: kotlin.String? = contentURI?.let { it1 -> uploader.getRealPathFromURI(it1) }
                        try
                        {
                            if (contentURI != null) {
                                android.widget.Toast.makeText(activity!!.applicationContext, path.toString(), android.widget.Toast.LENGTH_SHORT).show()
                                uploader.uploadImage(path.toString(),commentaire,
                                    com.example.corona.ui.Util.getProperty("urlPostVideoUser", context!!))
                            }
                        }
                        catch (e: java.io.IOException) {
                            e.printStackTrace()
                            android.widget.Toast.makeText(activity!!.applicationContext, "Failed!", android.widget.Toast.LENGTH_SHORT).show()
                        }

                    }


                }
            }catch (e:ConcurrentModificationException)
            {
                Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
            }

        }

    }
}
