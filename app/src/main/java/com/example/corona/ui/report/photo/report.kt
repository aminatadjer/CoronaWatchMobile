package com.example.corona.ui.report.photo


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.corona.R
import com.example.corona.ui.upload.UploadImage

import com.google.android.gms.tasks.TaskCompletionSource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.report_fragment.*
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class report : Fragment() {

    lateinit var uploader: UploadImage
    companion object {
        fun newInstance() = report()

        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET

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





           // activity!!.bottom_bar.visibility = View.GONE



        //activity!!.toolbar.visibility = View.GONE






        return inflater.inflate(com.example.corona.R.layout.report_fragment, container, false)
    }









    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)




        //requireActivity().onBackPressedDispatcher.addCallback




        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.toolbar_msg)
        end.visibility=View.GONE

        start.visibility=View.VISIBLE
        previewView.visibility=View.VISIBLE





        try {
            enableActions()
            setClickListeners()
            requestPermissions()
        }catch (e:ConcurrentModificationException)
        {
            Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
        }






        img_pick_btn.setOnClickListener {

            val galleryAction =
                reportDirections.galleryAction()
            Navigation.findNavController(it).navigate(galleryAction)
            //dispatchGalleryPictureIntent()


        }

        img_photo_btn.setOnClickListener {

            end.visibility=View.GONE

            start.visibility=View.VISIBLE
            previewView.visibility=View.VISIBLE
            try {
                setClickListeners()
                requestPermissions()
            }catch (e:ConcurrentModificationException)
            {
                Toast.makeText(context!!,"اعد المحاولة",Toast.LENGTH_LONG).show()
            }

        }

        img_video_btn.setOnClickListener {
            val takenvideoAction =
                reportDirections.takenVideoAction()
            Navigation.findNavController(it).navigate(takenvideoAction)

            //dispatchTakeVideoIntent()

        }
        viewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)

        // TODO: Use the ViewModel
    }


    private fun setClickListeners() {

        toggleCameraLens.setOnClickListener { toggleFrontBackCamera() }
        takePhoto.setOnClickListener {

            takePicture()




        }

    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {

            previewView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(activity!!,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )

        }
    }
    private var d:Uri?=null

    private var tcs =  TaskCompletionSource<Boolean>()

    private fun takePicture() {

        imageView.setImageResource(0)
        //disableActions()





        savePictureToFile()




        tcs.task.addOnSuccessListener {
              if (d != null){
                  uploader=UploadImage(activity!!)
                  imageView.setImageURI(d)
                  Toast.makeText(context,d!!.path.toString() , Toast.LENGTH_SHORT).show()
                  sendImage.setOnClickListener{
                      var a:String=textFieldImageReport.editText?.text.toString()
                      Toast.makeText(context, Environment.getExternalStorageDirectory().toString() +
                              File.separator , Toast.LENGTH_SHORT).show()
                      d!!.path?.let { it1 -> uploader.uploadImage(
                          Environment.getExternalStorageDirectory().toString() +
                              File.separator +it1,a)}
                  }

              }

        }



    }

    private fun savePictureToFile() {

        fileUtils.createDirectoryIfNotExist()
        val file = fileUtils.createFile()

        imageCapture!!.takePicture(file, getMetadata(), executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {

                    activity!!.runOnUiThread {



                        d=FileProvider.getUriForFile(context!!,
                            "com.example.corona.ui.post",
                            file)

                        tcs.trySetResult(true)

                        //enableActions()
                    }
                }

                override fun onError(imageCaptureError: ImageCapture.ImageCaptureError,
                                     message: String,
                                     cause: Throwable?) {
                    Toast.makeText(activity,
                        /*getString(R.string.image_capture_failed)*/"ee",
                        Toast.LENGTH_SHORT).show()
                }


            })







        start.visibility=View.GONE
        previewView.visibility=View.GONE
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


            try {
                val parent = previewView.parent as ViewGroup
                parent.removeView(previewView)
                parent.addView(previewView, 0)
                previewView.surfaceTexture = it.surfaceTexture
                updateTransform()
            }catch (e:Exception){

            }



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
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                previewView.post { startCamera() }
            } else {
                activity!!.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
    }




}




