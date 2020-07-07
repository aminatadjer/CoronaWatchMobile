package com.example.corona.ui.notification
import com.example.corona.ui.notification.entity.Notif
import retrofit2.Call
import retrofit2.http.*


interface Service {
    @GET("notification/")
    fun getAll(): Call<List<Notif>>

}