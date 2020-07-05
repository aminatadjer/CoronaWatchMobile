package com.example.corona.ui.map
import com.example.corona.ui.map.local.Hospital
import com.example.corona.ui.map.local.Region
import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("region/")
    fun getAll(): Call<List<Region>>

    @GET("centre/{region}/getByRegion/")
    fun getCentreByRegion(@Path("region") region: Int): Call<List<Hospital>>

    @GET("region/getRiskedZones/")
    fun getRisked(): Call<List<Region>>
}