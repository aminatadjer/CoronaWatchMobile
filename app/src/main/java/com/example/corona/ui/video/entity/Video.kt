package com.example.corona.ui.video.entity

import com.google.gson.annotations.SerializedName

class Video(
    @SerializedName("id") var id:Int,
    @SerializedName("media") var media:String,
    @SerializedName("commentaire") var commentaire:String,
    @SerializedName("date") var date:String

    ) {
}