package com.example.corona.ui.spider

import com.example.corona.ui.Util
import com.example.corona.ui.map.Hospital
import com.example.corona.ui.map.Region
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

        @GET("robot/getValidate")
        fun getAll(): Call<List<Publication>>


}