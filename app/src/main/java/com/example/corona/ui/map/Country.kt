package com.example.corona.ui.map

import com.google.gson.annotations.SerializedName

class Country(
              var id:Int,
              @SerializedName("Country") var Country:String?,
              @SerializedName("NewConfirmed") var NewConfirmed:Int,
              @SerializedName("TotalConfirmed") var TotalConfirmed:Int,
              @SerializedName("NewDeaths") var NewDeaths:Int,
              @SerializedName("TotalDeaths") var TotalDeaths:Int,
              @SerializedName("NewRecovered") var NewRecovered:Int,
              @SerializedName("TotalRecovered") var TotalRecovered:Int,
              @SerializedName("Date") var Date:String?,
              var kmlResource: Int =0) {
}