package com.example.corona.ui.view
import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("notification/")
    fun getAll(): Call<List<Video>>

}