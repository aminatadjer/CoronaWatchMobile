package com.example.corona.ui.post.services

import com.example.corona.ui.post.entity.Article
import retrofit2.Call
import retrofit2.http.*


interface Service {

    @GET("article/getValidate/")
    fun getAllArticles(): Call<List<Article>>

}