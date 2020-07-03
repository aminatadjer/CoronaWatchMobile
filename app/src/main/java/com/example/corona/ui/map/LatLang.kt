package com.example.corona.ui.map

import com.example.corona.R

object LatLang {
    val latLangAlgeria: HashMap<String, Region> = linkedMapOf(
        "1" to Region(1,"Adrar",28.0174,	0.2642,"أدرار",14,15,14,15,16,"1-1-2020",0, R.raw.adrar),
        "11" to Region(1,"tamarast",22.7903,	5.5193,"أدرار",14,15,14,15,16,"1-1-2020",2, R.raw.tamanghasset),
        "26" to Region(1,"Adrar",36.264169,	2.753926,"أدرار",14,15,14,15,16,"1-1-2020",1, R.raw.medea)


        )

    val latLangWorld: HashMap<String, Region> = linkedMapOf(

        "DZA" to Region(1,"DZA",28.0174,	0.2642,"أدرار",14,15,14,15,16,"1-1-2020",0, R.raw.dza),
        "FRA" to Region(1,"FRA",22.7903,	5.5193,"أدرار",14,15,14,15,16,"1-1-2020",2, R.raw.fra),
        "MAR" to Region(1,"MAR",36.264169,	2.753926,"أدرار",14,15,14,15,16,"1-1-2020",1, R.raw.mar)

    )
}