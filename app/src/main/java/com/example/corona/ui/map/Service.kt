package com.example.corona.ui.map
import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("region/")
    fun getAll(): Call<List<Region>>

    @GET("centre/{region}/getByRegion/")
    fun getCentreByRegion(@Path("region") region: Int): Call<List<Hospital>>
}