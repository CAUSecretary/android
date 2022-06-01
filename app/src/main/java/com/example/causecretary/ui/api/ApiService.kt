package com.example.causecretary.ui.api


import com.example.causecretary.ui.data.*
import com.example.causecretary.ui.data.dto.LoginRequestData
import com.example.causecretary.ui.data.dto.RegisterRequestData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

class ApiService {
    companion object{
        val DOMAIN = "http://3.34.136.35:9000/"
        val NAVIDOMAIN = "http://192.168.219.109:8080/"
    }
}

interface RetrofitApi{

    @Multipart
    @POST("test")
    fun postUsersMulti(@Part file: MultipartBody.Part): Call<String>

    @POST("auth/authlogin")
    fun adminlogin(@Body admin:LoginRequestData): Call<AdminResponse>


    @POST("users/register")
    fun postUsers(@Body body: RegisterRequestData): Call<RegisterResponse>


    @POST("auth/userlogin")
    fun login(@Body admin:LoginRequestData): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/uncertified")
    fun getUncertifiedList(@Header("X-ACCESS-TOKEN") jwt: String,@Field("userIdx") userIdx: Int):Call<AdminResponse>


    @FormUrlEncoded
    @PATCH("auth/certify")
    fun certify(@Field("userIdx")userIdx: Int,@Field("belong") belong: String): Call<CertifyResponse>

    @GET("get/each/event/main/{eventIdx}")
    fun getEventDetail(@Path("eventIdx")eventIdx: Int): Call<EventDetailResponse>

    @GET("get/all/event/main/1")
    fun getEventOff():Call<EventOffResponse>

    @GET("get/all/event/main/0")
    fun getEventOn():Call<EventOnResponse>



   // @GET("users/register")
    //fun getImageList(): Call<AdminResponse>

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