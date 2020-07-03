package com.example.corona.ui.view


import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.corona.R
import kotlinx.android.synthetic.main.diagnose_fragment.*
import java.util.*


class DiagnoseFragment : Fragment() {

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DiagnoseViewModel::class.java)





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
