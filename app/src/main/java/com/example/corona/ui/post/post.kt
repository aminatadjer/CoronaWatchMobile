package com.example.corona.ui.post

import android.Manifest
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

class post : Fragment() {



    companion object {
        fun newInstance() = post()

        internal const val REQUEST_CODE_PERMISSIONS = 10

        internal val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private val fileUtils: FileUtils by lazy { FileUtilsImpl() }
    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }

    private var imageCapture: ImageCapture? = null

    private var lensFacing = CameraX.LensFacing.BACK

    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.bottom_nav.visibility = View.GONE
        activity!!.toolbar.visibility = View.GONE

        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)


        setClickListeners()
        requestPermissions()
        // TODO: Use the ViewModel


    }
    private fun setClickListeners() {
        toggleCameraLens.setOnClickListener { toggleFrontBackCamera() }
        previewView.setOnClickListener { takePicture() }
        takenImage.setOnLongClickListener {

            return@setOnLongClickListener true
        }


    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {

            previewView.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(activity!!, REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS)

        }
    }

    private fun takePicture() {
        disableActions()
        savePictureToFile()

    }

    private fun savePictureToFile() {

        fileUtils.createDirectoryIfNotExist()
        val file = fileUtils.createFile()




        imageCapture!!.takePicture(file, getMetadata(), executor,
            object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(file: File) {
                    activity!!.runOnUiThread {
                        takenImage.setImageURI(
                            FileProvider.getUriForFile(context!!,
                                "com.example.corona.ui.post",
                                file))
                        enableActions()
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
    }

    private fun getMetadata() = ImageCapture.Metadata().apply {
        isReversedHorizontal = lensFacing == CameraX.LensFacing.FRONT
    }




    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun imageToBitmap(image: Image): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
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
        takenImage.isClickable = false
        toggleCameraLens.isClickable = false

    }

    private fun enableActions() {
        previewView.isClickable = true
        takenImage.isClickable = true
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
