package com.example.corona.ui.spider.entity

import com.google.gson.annotations.SerializedName

class Publication(
    @SerializedName("id") var id:Int,
    @SerializedName("url") var url:String,
    @SerializedName("type") var type:String?,
    @SerializedName("titre") var titre:String?,
    @SerializedName("description") var description:String?,
    @SerializedName("date") var date:String?

) {
}