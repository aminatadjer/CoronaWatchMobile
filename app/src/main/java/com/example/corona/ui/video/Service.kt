package com.example.corona.ui.video

import com.example.corona.ui.video.entity.Video
import retrofit2.Call
import retrofit2.http.GET

interface Service {

        @GET("video/getValidate")
        fun getAll(): Call<List<Video>>


}