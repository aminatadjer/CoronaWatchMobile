package com.example.corona.ui.view


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.corona.R
import com.example.corona.ui.view.UploadEtat
import com.example.corona.ui.upload.UploadImage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.diagnose_fragment.*
import me.ibrahimsn.lib.SmoothBottomBar
import java.io.ByteArrayOutputStream
import java.util.*


class DiagnoseFragment : Fragment() {
    private lateinit var mtitel: TextView
    lateinit var toolbar: SmoothBottomBar
    lateinit var uploader: UploadEtat
    val CAMERA_REQUEST = 2
    private var selctedPhoto: Uri?=null

    var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    companion object {
        fun newInstance() = DiagnoseFragment()
        const val TEMPERATURE_MIN = 35
        const val TEMPERATURE_MAX = 45
        const val HEART_BEAT_MIN = 45
        const val HEART_BEAT_MAX = 165
        const val WEIGHT_MIN = 20
        const val WEIGHT_MAX = 200

        var c = Calendar.getInstance()
        var year=c.get(Calendar.YEAR)
        var month=c.get(Calendar.MONTH)
        var day=c.get(Calendar.DAY_OF_MONTH)

    }


    private lateinit var viewModel: DiagnoseViewModel


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.diagnose_fragment, container, false)
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



                diagnose_button.setOnClickListener {
                    if (data != null)
                    {   uploader= com.example.corona.ui.view.UploadEtat(activity!!)
                        val contentURI = selctedPhoto
                        val temperature:String=temperature_seek_bar.getProgress().toString()
                        val rythmeCardiaque=heart_beat_seek_bar.getProgress().toString()
                        val poids=weight_number_piker.getValue().toString()
                        val path: kotlin.String? = contentURI?.let { it1 -> uploader.getRealPathFromURI(it1) }
                        try
                        {
                            if (contentURI != null) {
                                android.widget.Toast.makeText(activity!!.applicationContext, "تم تحميل صورتك بنجاح", android.widget.Toast.LENGTH_SHORT).show()
                                uploader.uploadEtat(path.toString(),poids,temperature,rythmeCardiaque, "2020-07-05T00:00:00","http://192.168.1.9:8000/api/etatSante/")
                            }
                        }
                        catch (e: java.io.IOException) {
                            e.printStackTrace()
                            android.widget.Toast.makeText(activity!!.applicationContext, "Failed!", android.widget.Toast.LENGTH_SHORT).show()
                        }

                    }
                }


            }catch (e:Exception)
            {
                Toast.makeText(context!!,"اعد المحاولة", Toast.LENGTH_LONG).show()
            }

        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DiagnoseViewModel::class.java)

        val tolb=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        mtitel=tolb.findViewById<TextView>(R.id.toolbar_title)
        mtitel.text= getString(R.string.santeTitle)

        toolbar = activity!!.findViewById(R.id.bottom_bar)
        toolbar.visibility=View.GONE


        val add_photo_diagno=activity!!.findViewById<FloatingActionButton>(R.id.add_photo_diagno)

        add_photo_diagno.setOnClickListener {



        }

        pickDateBtn.setOnClickListener {
            var dpd = DatePickerDialog(this!!.activity!!, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->

                dateTv.setText(""+ mDay +"/"+ (mMonth+1) +"/"+ mYear)

                viewModel.year = mYear
                viewModel.month = (mMonth+1)
                viewModel.day = mDay


            }, year, month, day)




            dpd.show()

            val ok: Button = dpd.getButton(DialogInterface.BUTTON_POSITIVE)
            ok.setTextColor(Color.rgb(255, 0, 0))

            val cancel: Button = dpd.getButton(DialogInterface.BUTTON_NEGATIVE)
            cancel.setTextColor(Color.rgb(255, 0, 0))
        }




        temperature_seek_bar.progress = 0
        temperature_seek_bar.max = TEMPERATURE_MAX - TEMPERATURE_MIN
        temperature_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                b: Boolean
            ) {
                val temperature = progress + TEMPERATURE_MIN
                temperature_value_view.text = temperature.toString()
                viewModel.temperature = temperature
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        heart_beat_seek_bar.progress = 0
        heart_beat_seek_bar.max = HEART_BEAT_MAX - HEART_BEAT_MIN
        heart_beat_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                b: Boolean
            ) {
                val heartBeat = progress + HEART_BEAT_MIN
                heart_beat_value_view.text = heartBeat.toString()
                viewModel.heartBeat = heartBeat

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        weight_number_piker.minValue = WEIGHT_MIN
        weight_number_piker.maxValue = WEIGHT_MAX
        weight_number_piker.value = WEIGHT_MIN
        weight_number_piker.setOnValueChangedListener { _, _, newVal ->
            viewModel.weight = newVal
        }


        diagnose_button.setOnClickListener {
            diagnose()
        }
    }

    private fun diagnose() {
        viewModel.uploadData(requireContext())


    }



}
