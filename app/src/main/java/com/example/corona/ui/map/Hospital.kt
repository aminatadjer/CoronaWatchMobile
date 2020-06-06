package com.example.corona.ui.map

import com.google.gson.annotations.SerializedName

class Hospital (
    @SerializedName("id") var id:Int?,
    @SerializedName("region") var region:Int?,
    @SerializedName("nom") var nom:String?,
    @SerializedName("numero") var numero:String,
    @SerializedName("adresse") var adresse:String?
) {


}