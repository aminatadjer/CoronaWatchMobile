package com.example.corona.ui.report

import android.Manifest
import android.app.Activity.RESULT_OK

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image

import android.net.Uri

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.Navigation


import com.example.corona.R
import com.example.corona.ui.MainActivity
import com.example.corona.ui.post.FileUtils
import com.example.corona.ui.post.FileUtilsImpl
import com.example.corona.ui.post.post

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_fragment.*
import kotlinx.android.synthetic.main.report_fragment.*
import kotlinx.android.synthetic.main.report_fragment.previewView

import kotlinx.android.synthetic.main.report_fragment.toggleCameraLens
import kotlinx.android.synthetic.main.takenimage_fragment.*
import kotlinx.android.synthetic.main.takenvideo_fragment.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.concurrent.thread


class report : Fragment() {




    private val lock = Object()

    val takenimageaction=reportDirections.takenImageAction()



    companion object {
        fun newInstance() = report()

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }

    private val fileUtils: FileUtils by lazy { FileUtilsImpl() }
    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }

    private var imageCapture: ImageCapture? = null

    private var lensFacing = CameraX.LensFacing.BACK

    private lateinit var viewModel: ReportViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





            activity!!.bottom_nav.visibility = View.GONE



        //activity!!.toolbar.visibility = View.GONE




        return inflater.inflate(R.layout.report_fragment, container, false)
    }









    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        end.visibility=View.GONE

        start.visibility=View.VISIBLE







        enableActions()
        setClickListeners()
        requestPermissions()




        img_pick_btn.setOnClickListener {

            val galleryAction = reportDirections.galleryAction()
            Navigation.findNavController(it).navigate(galleryAction)
            //dispatchGalleryPictureIntent()


        }

        img_photo_btn.setOnClickListener {

            end.visibility=View.GONE

            start.visibility=View.VISIBLE
            setClickListeners()
            requestPermissions()

        }

        img_video_btn.setOnClickListener {
            val takenvideoAction = reportDirections.takenVideoAction()
            Navigation.findNavController(it).navigate(takenvideoAction)

            //dispatchTakeVideoIntent()

        }
        viewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)

        // TODO: Use the ViewModel
    }


    private fun setClickListeners() {

        toggleCameraLens.setOnClickListener { toggleFrontBackCamera() }
        previewView.setOnClickListener {

            takePicture()




               // Navigation.findNavController(it).navigate(takenimageaction)



        }
       /* takenImage.setOnLongClickListener {

            return@setOnLongClickListener true
        }*/


    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {

            previewView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(activity!!, post.REQUIRED_PERMISSIONS,
                post.REQUEST_CODE_PERMISSIONS
            )

        }
    }

    private fun takePicture() {

        imageView.setImageResource(0)
        //disableActions()


        savePictureToFile()

    }

    private fun savePictureToFile() {

        fileUtils.createDirectoryIfNotExist()
        val file = fileUtils.createFile()

        imageCapture!!.takePicture(file, getMetadata(), executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    activity!!.runOnUiThread {


                        imageView.setImageURI(
                            FileProvider.getUriForFile(context!!,
                                "com.example.corona.ui.post",
                                file))
                        //enableActions()
                    }
                }
                override fun onError(imageCaptureError: ImageCapture.ImageCaptureError,
                                     message: String,
                                     cause: Throwable?) {
                    Toast.makeText(activity,
                        getString(R.string.image_capture_failed),
                        Toast.LENGTH_SHORT).show()
                }


            })

        start.visibility=View.GONE

        end.visibility=View.VISIBLE

    }

    private fun getMetadata() = ImageCapture.Metadata().apply {
        isReversedHorizontal = lensFacing == CameraX.LensFacing.FRONT
    }






    private fun toggleFrontBackCamera() {
        lensFacing = if (lensFacing == CameraX.LensFacing.BACK) {
            CameraX.LensFacing.FRONT
        } else {
            CameraX.LensFacing.BACK
        }
        previewView.post { startCamera() }
    }

    private fun startCamera() {
        CameraX.unbindAll()


        val preview = createPreviewUseCase()

        preview.setOnPreviewOutputUpdateListener {

            val parent = previewView.parent as ViewGroup
            parent.removeView(previewView)
            parent.addView(previewView, 0)

            previewView.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        imageCapture = createCaptureUseCase()
        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun createPreviewUseCase(): Preview {
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetRotation(previewView.display.rotation)

        }.build()

        return Preview(previewConfig)
    }

    private fun createCaptureUseCase(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setLensFacing(lensFacing)
                setTargetRotation(previewView.display.rotation)
                setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            }


        return ImageCapture(imageCaptureConfig.build())
    }




    private fun updateTransform() {
        val matrix = Matrix()

        val centerX = previewView.width / 2f
        val centerY = previewView.height / 2f

        val rotationDegrees = when (previewView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        previewView.setTransform(matrix)
    }

    private fun disableActions() {
        previewView.isClickable = false
       // takenImage.isClickable = false
        toggleCameraLens.isClickable = false

    }

    private fun enableActions() {
        previewView.isClickable = true
        //takenImage.isClickable = true
        toggleCameraLens.isClickable = true

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == post.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                previewView.post { startCamera() }
            } else {
                activity!!.finish()
            }
        }
    }

    private fun allPermissionsGranted() = post.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
    }




}




