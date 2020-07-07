package com.example.corona.ui.Diagnose

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.corona.ui.Diagnose.DiagnoseFragment

class DiagnoseViewModel : ViewModel() {
    var temperature: Int = DiagnoseFragment.TEMPERATURE_MIN
    var heartBeat: Int = DiagnoseFragment.HEART_BEAT_MIN
    var weight: Int = DiagnoseFragment.WEIGHT_MIN
    var year: Int = DiagnoseFragment.year
    var month: Int = DiagnoseFragment.month
    var day: Int = DiagnoseFragment.day


    fun uploadData(context : Context){
        Toast.makeText(context, "$temperature $heartBeat $weight $year $month $day", Toast.LENGTH_SHORT).show()

    }

}
