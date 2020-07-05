package com.example.corona.ui.view
import com.example.corona.ui.view.entity.Video
import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("notification/")
    fun getAll(): Call<List<Video>>

}