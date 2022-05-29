package com.example.causecretary.ui.api

import com.example.causecretary.ui.data.RegisterResponse
import com.example.causecretary.ui.data.dto.RegisterRequestData
import retrofit2.Call
import retrofit2.http.*

class ApiService {
    companion object{
        val DOMAIN = "http://192.168.138.85:9000/"
        val NAVIDOMAIN = "http://192.168.219.106:8080/"
    }
}

interface RetrofitApi{



    @POST("users/register")
    fun postUsers(@Body body: RegisterRequestData): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field ("email")email:String, @Field("password") password: String): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/search/weigh")
    fun searchRoute_weigh(
        @Field("endPoint") endPoint:String,
        @Field("startLat") curLat:String,
        @Field("startLon") curLon:String
    ) : Call<String>

    @FormUrlEncoded
    @POST("/search/distance")
    fun searchRoute_distance(
        @Field("endPoint") endPoint:String,
        @Field("startLat") curLat:String,
        @Field("startLon") curLon:String
    ) : Call<String>

}