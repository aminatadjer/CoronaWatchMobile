package com.example.corona.ui.video

import com.google.gson.annotations.SerializedName

class Video(
    @SerializedName("id") var id:Int,
    @SerializedName("url") var url:String
    ) {
}