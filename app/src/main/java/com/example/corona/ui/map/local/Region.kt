package com.example.corona.ui.map.local

import com.google.gson.annotations.SerializedName

class Region(
             @SerializedName("id") var id:Int,
             @SerializedName("nom") var nom:String?,
             @SerializedName("lat") var lat:Double,
             @SerializedName("lang") var lang:Double,
             @SerializedName("ArabicName") var ArabicName:String,
             @SerializedName("suspect") var suspect:Int,
             @SerializedName("confirme") var confirme:Int,
             @SerializedName("critique") var critique:Int,
             @SerializedName("mort") var mort:Int,
             @SerializedName("guerie") var guerie:Int,
             @SerializedName("date_validation") var date_validation:String?,
             @SerializedName("degre") var degre:Int,
             var kmlResource: Int =0
) {

}