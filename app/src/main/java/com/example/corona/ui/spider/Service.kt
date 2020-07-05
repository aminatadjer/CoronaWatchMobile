package com.example.corona.ui.spider

import com.example.corona.ui.spider.entity.Publication
import retrofit2.Call
import retrofit2.http.GET

interface Service {

        @GET("robot/getValidate")
        fun getAll(): Call<List<Publication>>


}